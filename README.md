# Relative Interval timer app
Interval‑based reminder app for Android.

> **WARNING: This was created with help of AI**
> 
> This README and codebase were created using Agentic AI.

Real human here: ah yes just casually wasted 2 days creating this instead of actually changing my routine. Why change routine when you can change alarms accordingly or relatively or uh idk words not loading brain not braining enough. I created this for my personal use. you are free to use it in your billion dollar business. if you get too rich please share some money with me. Thanks.

Anyways here continues what AI wrote:

# What This App Actually Solves

Most reminder apps assume your life runs on a neat little timetable. Mine doesn't. Yours clearly doesn’t. Anyone whose medication schedule depends on when they take the first dose knows this pain: take pill #1 at 5:00 AM, everything shifts; take it at 7:12 AM, the whole schedule shifts again.

Regular alarm apps can’t handle this. They want fixed times, not “start counting from whenever I actually remember to take my first pill.” This app exists because real-world medication routines aren’t fixed; they’re flexible chains of intervals.

This project was built for people whose routines look like:

“Take this pill… then 30 minutes later take that pill… then 30 minutes later eat… then 4 hours later take the next thing… then 10 minutes… then 4 more hours…” Basically pomodoro with extra steps.

“Start whenever I manage to start. Everything else follows automatically.”

If you’ve tried to manage this with normal alarms, you know you end up with a messy forest of alarms that all go off at the wrong times because your day started differently. This app fixes that by letting you define relative timing, not fixed timestamps.

# Who This App Is For

People with variable-start medication routines

Anyone juggling complex dose sequences where the timing between steps matters more than the clock time

Folks with changing daily schedules

Caregivers managing medication for others

Pomodoro app that works like a proper alarm and different intervals.

People who want interval-based reminders for cooking, productivity cycles, workouts, or task chains

Anyone who’s silently yelling “why does no timer app just do this one simple thing”

If your routine is tied to when you begin, not what time it is, this app is built for you.

# Why existing apps don’t work for this

Normal alarm clock -> Fixed times only

Medisafe, MyTherapy etc. -> Still expect you to set absolute times or “every 8 hours from fixed start”

Countdown timer apps -> Die when screen off, no sequencing, no persistence

Workout timer apps -> Great for intervals but no snooze/dismiss logic, useless for other things

## App Overview

Relative Interval Timer lets you create flexible chains of alarms/reminders that trigger at intervals you define, starting from any time you choose.

## Main Features

- **Material Design UI:** Modern, clean interface with real-time countdowns and progress indicators. Dark and light themes supported.
- **Interval Chains:** Create sequences of alarms, each with its own duration (hours, minutes, seconds). Start the chain at any time; all subsequent alarms follow your custom intervals.
- **Reorder & Clone:** Drag to reorder alarms, clone any alarm for fast duplication, and delete with one tap.
- **Naming:** Give each alarm a custom name for clarity (e.g., “Take Pill”, “Eat”, “Workout”).
- **Playback Controls:** Pause/resume the entire sequence, skip forward/back, or stop at any time.
- **Reliable Background Service:** Alarms run reliably even if the app is closed, device is locked, or in Do Not Disturb mode (with permissions).
- **Persistent Storage:** All alarm chains are saved automatically. Restore your routine after device restarts.
- **Notifications:** High-priority notifications for alarms, with controls to pause, skip, or dismiss directly from the notification shade.
- **Validation:** Prevents invalid alarm chains (e.g., zero duration, duplicate times).

## Typical Use Cases

- Medication schedules with variable start times
- Pomodoro or productivity cycles
- Multi-stage cooking timers
- Interval training/workouts
- Task chains that depend on when you begin

## How to Use

1. Tap **Add Alarm** to create a new interval.
2. Set the duration and (optionally) a name.
3. Use quick add buttons for common intervals or enter a custom time.
4. Drag to reorder alarms, clone, or delete as needed.
5. Tap **Start** to begin the sequence. The first alarm starts immediately; others follow at your set intervals.
6. Use playback controls (Pause, Resume, Next, Previous, Stop) as needed during the sequence.
7. Dismiss alarms when they ring to continue to the next.

## Requirements

- Android 10 (API 29) or newer
- Permissions: Notifications, Foreground Service, Autostart (recommended for reliability)

## Notes

- The app uses a foreground service for reliable alarms. If you use battery optimization, whitelist the app for best results.
- Alarms can override silent/DND mode if you grant permission.
- All data is stored locally; no cloud sync or account required.

---

For feedback, issues, or feature requests, open an issue or contact the developer.