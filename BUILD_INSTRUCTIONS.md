# How to Build APK - Quick Guide

## Prerequisites

You need one of the following:
- **Android Studio** (easiest option)
- **Android SDK Command-line Tools**

## Method 1: Android Studio (Recommended for Beginners)

### Step 1: Install Android Studio
1. Download from: https://developer.android.com/studio
2. Run the installer
3. Complete the setup wizard (install all recommended components)

### Step 2: Open Project
1. Launch Android Studio
2. Click **"Open"**
3. Navigate to: `C:\Users\Om Singh\.gemini\antigravity\scratch\medicine-reminder`
4. Click **"OK"**
5. Wait for Gradle sync (bottom status bar)

### Step 3: Build APK
1. Menu: **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
2. Wait for build to finish
3. Click **"locate"** in the notification popup
4. Your APK is ready!

**APK Location:** `app\build\outputs\apk\debug\app-debug.apk`

## Method 2: Command Line (Advanced)

### Prerequisites
- Install Android SDK command-line tools
- Set `ANDROID_HOME` environment variable
- Add `%ANDROID_HOME%\tools\bin` to PATH

### Build Commands
```powershell
# Navigate to project
cd "C:\Users\Om Singh\.gemini\antigravity\scratch\medicine-reminder"

# Build debug APK
.\gradlew assembleDebug

# APK location:
# app\build\outputs\apk\debug\app-debug.apk
```

## Method 3: Use Another Computer

If you don't have Android Studio:
1. Copy the entire `medicine-reminder` folder to a USB drive
2. Use Android Studio on another PC/laptop
3. Build the APK there
4. Copy the APK back to your PC
5. Transfer to your phone

## Installing on Phone

1. **Transfer APK to phone** (via USB, email, or cloud)
2. **Enable installation from unknown sources:**
   - Settings → Apps → Special permissions → Install unknown apps
   - Select your file manager → Enable
3. **Open APK file** on phone and tap Install
4. **Grant permissions** when app first runs

## Need Help?

If you're stuck at any step, let me know! I can:
- Guide you through Android Studio installation
- Help troubleshoot build errors
- Provide alternative methods
