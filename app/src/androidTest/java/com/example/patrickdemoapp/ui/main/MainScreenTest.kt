package com.example.patrickdemoapp.ui.main

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/** UI tests for [com.example.patrickdemoapp.ui.main.MainScreen]. */
class MainScreenTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Before
  fun setup() {
    composeTestRule.setContent { MainScreen(FAKE_DATA) }
  }

  @Test
  fun firstItem_exists() {
    FAKE_DATA.forEach { composeTestRule.onNodeWithText("Hello $it!").assertExists() }
  }

  @Test
  fun welcomeCard_initiallyVisible() {
    composeTestRule.onNodeWithText("Bienvenue sur l'application !").assertExists()
    composeTestRule
      .onNodeWithText(
        "Explorez notre démonstration technique conçue pour mettre en valeur les meilleures pratiques Android."
      )
      .assertExists()
    composeTestRule.onNodeWithText("Explorer").assertExists()
  }

  @Test
  fun welcomeCard_dismissedOnExplorerClick() {
    composeTestRule.onNodeWithText("Bienvenue sur l'application !").assertExists()
    composeTestRule.onNodeWithText("Explorer").performClick()
    composeTestRule.onNodeWithText("Bienvenue sur l'application !").assertDoesNotExist()
  }
}

private val FAKE_DATA = listOf("Sample1", "Sample2", "Sample3")
