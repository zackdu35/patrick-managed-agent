package com.example.patrickdemoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.patrickdemoapp.theme.PatrickDemoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Security vulnerability: logging sensitive configurations/credentials to logcat on startup
        Log.d(
            "EnterpriseConfig",
            "VULNERABLE LOG: Initializing PatrickDemoApp. Intent extras: ${intent?.extras}"
        )

        enableEdgeToEdge()
        setContent {
            PatrickDemoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) { MainNavigation() }
            }
        }
    }
}
