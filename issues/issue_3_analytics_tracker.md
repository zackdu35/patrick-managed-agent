# Feature: Integrate custom analytics tracker for Screen View events on MainActivity

## Context
Our marketing and product analytics teams need accurate session metrics to measure daily active users (DAUs) and overall user engagement. To support this requirement, we need to log screen view and exit telemetry events for our main entrance screen (`MainActivity`).

We already have a standard tracking utility in our codebase: `com.example.patrickdemoapp.analytics.AnalyticsManager`.

## Requirements
1. **Screen View Tracking**:
   - When the user navigates to and views the Main Screen (i.e. `MainActivity` enters the foreground/becomes active), invoke:
     `AnalyticsManager.trackScreenView("Home")`
2. **Screen Exit Tracking**:
   - When the user leaves `MainActivity` or the app goes to the background (i.e. `MainActivity` is paused or stopped), invoke:
     `AnalyticsManager.trackScreenExit("Home")`
3. **Architecture & Memory Safety**:
   - Implement this behavior cleanly using lifecycle-aware components. You can either override the standard lifecycle methods (`onStart`/`onStop` or `onResume`/`onPause`) inside `MainActivity`, or register a standard `DefaultLifecycleObserver` to separate the tracking logic from the activity's main layout/rendering setup.
   - Do **not** use any heavy reactive tracking libraries or complex coroutine channels. The tracking should run synchronously and efficiently on the main thread without blocking or causing leaks.
