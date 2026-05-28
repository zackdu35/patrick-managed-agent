package com.example.patrickdemoapp

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Main : NavKey
@Serializable data object Dashboard : NavKey
@Serializable data object Settings : NavKey
