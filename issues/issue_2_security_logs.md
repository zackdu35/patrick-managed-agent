# Security / Bug: Prevent password or API keys from being leaked in debug logs on MainActivity startup

## Context
During a recent static and dynamic security audit of our mobile applications, our InfoSec team flagged a compliance failure concerning **Data Leakage via Logcat**. The report indicates that the application logs sensitive intent parameters and bundled configurations immediately on startup. If this app is launched in a corporate setting with single-sign-on or MDM extras, these logs could reveal private authentication parameters.

## Identified Issue
In `MainActivity.kt` under the `onCreate` method, a verbose logging call dumps the contents of `intent.extras` directly to the system log with the tag `"EnterpriseConfig"`. 

This must be corrected before we can submit the new release to the security committee.

## Requirements
1. **Sanitize Logcat**: Remove or completely sanitize the `android.util.Log.d` call that outputs raw intent extras or configurations to Logcat during the startup flow.
2. **Environment Filtering**: Ensure that debug or verbose logging is restricted based on the build type (e.g. log only in local `debug` configurations and never in production/release environments).
3. **No Over-Engineering**: Avoid introducing heavy third-party logging framework dependencies (like Timber or Logback) unless they are already present in the build configurations, as we want to keep our APK size within corporate thresholds. Keep the fix simple, direct, and secure.
