package com.example.patrickdemoapp.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    var verboseLoggingEnabled by remember { mutableStateOf(true) }
    var sslPinningEnabled by remember { mutableStateOf(false) }
    var telemetryEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Corporate Settings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Configure enterprise parameters and security credentials.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Toggle 1
        SettingRow(
            label = "Verbose Debug Logging",
            description = "Logs raw bundle payloads to Logcat (InfoSec Warning: audit required)",
            checked = verboseLoggingEnabled,
            onCheckedChange = { verboseLoggingEnabled = it }
        )

        // Toggle 2
        SettingRow(
            label = "Strict SSL Pinning",
            description = "Blocks unverified proxy connections to backend servers",
            checked = sslPinningEnabled,
            onCheckedChange = { sslPinningEnabled = it }
        )

        // Toggle 3
        SettingRow(
            label = "Background Telemetry",
            description = "Transmits session performance reports to telemetry centers",
            checked = telemetryEnabled,
            onCheckedChange = { telemetryEnabled = it }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Version info
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Patrick Enterprise Demo App",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Build Version 1.0.42 (Legacy SVN build)",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SettingRow(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                role = Role.Switch,
                onValueChange = onCheckedChange
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = null // Handled by toggleable Row modifier
        )
    }
}
