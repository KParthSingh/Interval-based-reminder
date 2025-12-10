# Reminder App
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

“Take this pill… then 30 minutes later take that pill… then 30 minutes later eat… then 4 hours later take the next thing… then 4 more hours… then 4 more hours…”

“Start whenever I manage to start. Everything else follows automatically.”

If you’ve tried to manage this with normal alarms, you know you end up with a messy forest of alarms that all go off at the wrong times because your day started differently. This app fixes that by letting you define relative timing, not fixed timestamps.

# Who This App Is For

People with variable-start medication routines

Anyone juggling complex dose sequences where the timing between steps matters more than the clock time

Folks with changing daily schedules

Caregivers managing medication for others

People who want interval-based reminders for cooking, productivity cycles, workouts, or task chains

Anyone who’s silently yelling “why does no timer app just do this one simple thing”

If your routine is tied to when you begin, not what time it is, this app is built for you.

# Why existing apps don’t work for this

Normal alarm clock -> Fixed times only

Medisafe, MyTherapy etc. -> Still expect you to set absolute times or “every 8 hours from fixed start”

Countdown timer apps -> Die when screen off, no sequencing, no persistence

Workout timer apps -> Great for intervals but no medication-style snooze/dismiss logic

## Overview

Medicine Reminder App enables users to create sequences of alarms that trigger automatically at specified intervals. The application provides reliable background execution, comprehensive playback controls, and real-time progress tracking.

## Use Cases

- Multi-medication schedules with specific intervals
- Interval training and workout timers
- Multi-stage cooking timers
- Sequential task reminders
- Pomodoro-style study sessions
- Daily health routine reminders

## Key Features

**Alarm Sequencing**
- Chain multiple alarms to trigger sequentially
- Set precise durations (hours, minutes, seconds)
- Quick add buttons for common time increments
- Reorder alarms via drag and drop
- Optional naming for each alarm

**Playback Controls**
- Pause and resume entire sequences
- Navigate forward/backward through alarm chain
- Stop sequences or dismiss individual alarms
- Start individual alarms independently for testing

**Background Reliability**
- Foreground service ensures consistent operation
- Functions when device is locked or screen is off
- Overrides silent/DND mode (with permissions)
- Persists through device restarts

**User Interface**
- Material Design 3 implementation
- Real-time countdown display
- Visual progress indicators
- Pause state synchronization
- Notification controls for all playback functions

**Data Management**
- Automatic persistent storage
- Clone alarms for quick duplication
- Delete and reorder operations
- Validation for invalid configurations

## Requirements

- Android 10 (API 29) or higher
- Permissions: Notifications, Exact Alarms, Foreground Service

**Creating Sequences**
1. Tap "New Alarm" to add alarms
2. Configure duration and optional name
3. Use quick add buttons or manual time entry
4. Reorder as needed
5. Tap "START SEQUENCE"

**Active Sequence Controls**
- Pause/Resume: Temporarily halt countdown
- Next/Prev: Navigate between alarms
- Stop: End entire sequence
- Dismiss: Acknowledge ringing alarm and proceed

**Management**
- Arrow buttons to reorder
- Plus button to clone
- Trash icon to delete
- Tap fields to edit