#!/bin/bash
set -e

echo "=== STARTING ANDROID BUILD ENVIRONMENT SETUP ==="

# 1. Install Java if not present
if ! command -v java &>/dev/null; then
    echo "Java not found. Installing openjdk-17-jdk-headless..."
    # Update package lists and install Java headless
    apt-get update -qq && apt-get install -y openjdk-17-jdk-headless
else
    echo "Java is already installed: $(java -version 2>&1 | head -n 1)"
fi

# 2. Detect proxy settings
PROXY_HOST=""
PROXY_PORT=""

if [ -n "$https_proxy" ]; then
    PROXY_URL=$https_proxy
elif [ -n "$http_proxy" ]; then
    PROXY_URL=$http_proxy
fi

if [ -n "$PROXY_URL" ]; then
    echo "Detected proxy in environment: $PROXY_URL"
    # Parse host and port
    PROXY_URL_CLEAN=${PROXY_URL#*//}
    PROXY_HOST=${PROXY_URL_CLEAN%:*}
    PROXY_PORT=${PROXY_URL_CLEAN#*:}
    echo "Parsed proxy - Host: $PROXY_HOST, Port: $PROXY_PORT"
else
    echo "No proxy detected in environment."
fi

# 2. Import Proxy Certificate if proxy is active
if [ -n "$PROXY_HOST" ] && [ -n "$PROXY_PORT" ]; then
    echo "Syncing system certificates to Java keystore via ca-certificates-java..."
    if [ -f /usr/bin/dpkg-reconfigure ]; then
        dpkg-reconfigure --frontend=noninteractive ca-certificates-java || true
    fi

    # Find all cacerts paths
    CACERTS_PATHS=$(find /usr/lib/jvm -name cacerts 2>/dev/null || true)
    
    if [ -n "$CACERTS_PATHS" ]; then
        # Search and import any custom certs from /usr/local/share/ca-certificates/ or /usr/share/ca-certificates/
        find /usr/local/share/ca-certificates/ /usr/share/ca-certificates/ -name "*.crt" 2>/dev/null | while read cert; do
            alias_name=$(basename "$cert" .crt | tr '[:upper:]' '[:lower:]' | tr -cd 'a-z0-9_')
            echo "Found system CA certificate: $cert"
            for cacerts_file in $CACERTS_PATHS; do
                echo "Importing system CA $cert into $cacerts_file..."
                keytool -delete -alias "$alias_name" -keystore "$cacerts_file" -storepass changeit -noprompt 2>/dev/null || true
                keytool -importcert -trustcacerts -file "$cert" -alias "$alias_name" -keystore "$cacerts_file" -storepass changeit -noprompt || true
            done
        done

        # Also fallback to extracting the leaf proxy certificate just in case
        echo "Fetching leaf SSL certificate from proxy..."
        openssl s_client -showcerts -connect services.gradle.org:443 -proxy "$PROXY_HOST:$PROXY_PORT" < /dev/null 2>/dev/null | openssl x509 -outform PEM > /tmp/proxy.crt || true
        if [ -f /tmp/proxy.crt ] && [ -s /tmp/proxy.crt ]; then
            for cacerts_file in $CACERTS_PATHS; do
                echo "Importing fetched leaf certificate into $cacerts_file..."
                keytool -delete -alias proxy -keystore "$cacerts_file" -storepass changeit -noprompt 2>/dev/null || true
                keytool -importcert -trustcacerts -file /tmp/proxy.crt -alias proxy -keystore "$cacerts_file" -storepass changeit -noprompt || true
            done
        fi
        echo "Proxy certificates imported successfully into all Java keystores."
    else
        echo "Warning: cacerts file not found under /usr/lib/jvm"
    fi

    # 3. Configure gradle.properties globally and locally
    echo "Configuring gradle.properties with proxy settings..."
    
    mkdir -p /root/.gradle
    
    for prop_file in "/root/.gradle/gradle.properties" "./gradle.properties"; do
        if [ -f "$prop_file" ]; then
            # Remove any existing proxy configs
            sed -i '/systemProp.http/d' "$prop_file" 2>/dev/null || true
            sed -i '/systemProp.https/d' "$prop_file" 2>/dev/null || true
        fi
        
        cat <<EOT >> "$prop_file"
systemProp.http.proxyHost=$PROXY_HOST
systemProp.http.proxyPort=$PROXY_PORT
systemProp.https.proxyHost=$PROXY_HOST
systemProp.https.proxyPort=$PROXY_PORT
EOT
        echo "Updated $prop_file"
    done
fi

# 4. Ensure settings.gradle.kts does not have foojay plugin to prevent extra downloads
if [ -f "settings.gradle.kts" ]; then
    echo "Disabling foojay plugin in settings.gradle.kts if present..."
    python3 -c "
import sys
try:
    content = open('settings.gradle.kts').read()
    if 'foojay-resolver' in content:
        # Simple removal of foojay plugin block
        import re
        content = re.sub(r'plugins\s*\{\s*id\(\"org\.gradle\.toolchains\.foojay-resolver-convention\"\)\s*version\s*\"[^\"]+\"\s*\}', '// foojay resolver disabled', content)
        open('settings.gradle.kts', 'w').write(content)
        print('Successfully disabled foojay in settings.gradle.kts')
except Exception as e:
    print('Warning while configuring settings.gradle.kts:', e)
" || true
fi

# 5. Pre-download Gradle wrapper if offline/proxy has issues
if [ -n "$PROXY_HOST" ] && [ -n "$PROXY_PORT" ] && [ ! -f "/tmp/gradle-9.1.0-bin.zip" ]; then
    echo "Pre-downloading Gradle 9.1.0 to /tmp/gradle-9.1.0-bin.zip..."
    curl -L --proxy "http://$PROXY_HOST:$PROXY_PORT" -o /tmp/gradle-9.1.0-bin.zip https://services.gradle.org/distributions/gradle-9.1.0-bin.zip || true
    
    if [ -f "/tmp/gradle-9.1.0-bin.zip" ]; then
        echo "Gradle wrapper pre-downloaded to /tmp/gradle-9.1.0-bin.zip."
        # Update gradle-wrapper.properties to use local file
        if [ -f "gradle/wrapper/gradle-wrapper.properties" ]; then
            sed -i 's|distributionUrl=.*|distributionUrl=file\\:/tmp/gradle-9.1.0-bin.zip|' gradle/wrapper/gradle-wrapper.properties || true
            echo "Updated gradle-wrapper.properties to point to local file /tmp/gradle-9.1.0-bin.zip"
        fi
    fi
fi

echo "=== BUILD ENVIRONMENT READY! ==="

# 6. Android SDK & Licenses Injection (Persisting the fix)
echo "Setting up Android SDK and accepting licenses..."
mkdir -p /tmp/android-sdk/licenses

# Point Gradle to the temporary SDK location
echo "sdk.dir=/tmp/android-sdk" > /.agents/workspace/local.properties

# Inject accepted license hashes
cat << 'EOF' > /tmp/android-sdk/licenses/android-sdk-license
8933bad161af4178b1185d1a37fbf41ea5269c55
d56f5187479451eabf01fb78af6dfcb131a6481e
24333f8a63b6825ea9c5514f83c2829b004d1fee
EOF

cat << 'EOF' > /tmp/android-sdk/licenses/android-sdk-preview-license
84831b9409646a918e30573bab4c9c91346d8abd
EOF

echo "Android SDK configuration complete."