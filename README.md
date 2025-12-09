# Medicine Reminder App

A native Android app for managing medicine reminders with flexible interval-based alarms.

## Step 1 Features
- Three test alarm buttons:
  - Start Alarm Now (immediate)
  - Alarm in 10 Seconds
  - Alarm in 20 Seconds
- Alarms work even when:
  - Phone is locked
  - Screen is off
  - Phone is in silent mode
- Foreground service for background reliability
- Full-screen alarm display

## Building the APK

1. Install Android Studio or Android SDK command-line tools
2. Navigate to the project directory:
   ```
   cd medicine-reminder
   ```
3. Build the debug APK:
   ```
   gradlew assembleDebug
   ```
4. The APK will be generated at:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

## Installation

1. Transfer the APK to your Android device
2. Enable "Install from Unknown Sources" in settings
3. Install the APK
4. Grant permissions when prompted:
   - Notifications
   - Exact alarms (Android 12+)

## Testing

1. Test immediate alarm with "Start Alarm Now"
2. Test delayed alarms with 10s and 20s buttons
3. Lock your phone and verify alarms still ring
4. Set phone to silent mode and verify alarms bypass silent mode

## Requirements

- Android 10 (API 29) or higher
- Compatible with Redmi Note 7 and Redmi A4

## Next Steps

Future updates will add:
- Preset creation with custom intervals
- Multiple presets with names
- Drag-and-drop interval management
- Persistent alarm scheduling
