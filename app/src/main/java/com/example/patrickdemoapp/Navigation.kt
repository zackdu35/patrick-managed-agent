package com.example.patrickdemoapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.patrickdemoapp.ui.main.MainScreen
import com.example.patrickdemoapp.ui.dashboard.DashboardScreen
import com.example.patrickdemoapp.ui.settings.SettingsScreen

@Composable
fun MainNavigation() {
  val backStack = rememberNavBackStack(Main)
  val currentKey = backStack.lastOrNull() ?: Main

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = {
      NavigationBar {
        NavigationBarItem(
          selected = currentKey == Main,
          onClick = {
            if (currentKey != Main) {
              backStack.removeLastOrNull()
              backStack.add(Main)
            }
          },
          icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
          label = { Text("Home") }
        )
        NavigationBarItem(
          selected = currentKey == Dashboard,
          onClick = {
            if (currentKey != Dashboard) {
              backStack.removeLastOrNull()
              backStack.add(Dashboard)
            }
          },
          icon = { Icon(Icons.Default.Star, contentDescription = "Dashboard") },
          label = { Text("Dashboard") }
        )
        NavigationBarItem(
          selected = currentKey == Settings,
          onClick = {
            if (currentKey != Settings) {
              backStack.removeLastOrNull()
              backStack.add(Settings)
            }
          },
          icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
          label = { Text("Settings") }
        )
      }
    }
  ) { paddingValues ->
    NavDisplay(
      backStack = backStack,
      onBack = { backStack.removeLastOrNull() },
      entryProvider =
        entryProvider {
          entry<Main> {
            Surface(
              modifier = Modifier.fillMaxSize(),
              color = MaterialTheme.colorScheme.background
            ) {
              MainScreen(
                onItemClick = { navKey -> backStack.add(navKey) },
                modifier = Modifier.padding(paddingValues)
              )
            }
          }
          entry<Dashboard> {
            Surface(
              modifier = Modifier.fillMaxSize(),
              color = MaterialTheme.colorScheme.background
            ) {
              DashboardScreen(
                modifier = Modifier.padding(paddingValues)
              )
            }
          }
          entry<Settings> {
            Surface(
              modifier = Modifier.fillMaxSize(),
              color = MaterialTheme.colorScheme.background
            ) {
              SettingsScreen(
                modifier = Modifier.padding(paddingValues)
              )
            }
          }
        },
    )
  }
}
