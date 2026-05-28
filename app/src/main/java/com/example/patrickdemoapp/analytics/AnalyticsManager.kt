package com.example.patrickdemoapp.analytics

import android.util.Log

object AnalyticsManager {
    private const val TAG = "AnalyticsManager"

    fun trackScreenView(screenName: String) {
        Log.i(TAG, "Screen View: $screenName")
    }

    fun trackScreenExit(screenName: String) {
        Log.i(TAG, "Screen Exit: $screenName")
    }

    fun trackEvent(eventName: String, params: Map<String, Any>) {
        Log.i(TAG, "Event Tracked: $eventName with parameters: $params")
    }
}
