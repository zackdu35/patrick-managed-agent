package com.example.patrickdemoapp.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.example.patrickdemoapp.data.DefaultDataRepository
import com.example.patrickdemoapp.theme.PatrickDemoAppTheme

@Composable
fun MainScreen(
  onItemClick: (NavKey) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: MainScreenViewModel = viewModel { MainScreenViewModel(DefaultDataRepository()) },
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  when (state) {
    MainScreenUiState.Loading -> {
      // Blank
    }
    is MainScreenUiState.Success -> {
      MainScreen(data = (state as MainScreenUiState.Success).data, modifier = modifier)
    }
    is MainScreenUiState.Error -> {
      Text("Error loading data: ${(state as MainScreenUiState.Error).throwable.message}")
    }
  }
}

@Composable
internal fun MainScreen(data: List<String>, modifier: Modifier = Modifier) {
  var isWelcomeCardVisible by rememberSaveable { mutableStateOf(true) }

  Column(modifier = modifier) {
    AnimatedVisibility(
      visible = isWelcomeCardVisible,
      enter = fadeIn() + expandVertically(),
      exit = fadeOut() + shrinkVertically()
    ) {
      ElevatedCard(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Column(
          modifier = Modifier
            .padding(16.dp)
        ) {
          Text(
            text = "Bienvenue sur l'application !",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Explorez notre démonstration technique conçue pour mettre en valeur les meilleures pratiques Android.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(16.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            Button(onClick = { isWelcomeCardVisible = false }) {
              Text(text = "Explorer")
            }
          }
        }
      }
    }
    data.forEach { Greeting(it) }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
  PatrickDemoAppTheme { MainScreen(listOf("Android")) }
}

@Preview(showBackground = true, widthDp = 340)
@Composable
fun MainScreenPortraitPreview() {
  PatrickDemoAppTheme { MainScreen(listOf("Android")) }
}
