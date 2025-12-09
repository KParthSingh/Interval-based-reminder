package com.medicinereminder.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationHelper {
    const val CHANNEL_ID = "medicine_reminder_channel"
    const val NOTIFICATION_ID = 1001
    const val COUNTDOWN_NOTIFICATION_ID = 1002

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val descriptionText = context.getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH // Changed from DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                setSound(null, null) // We handle sound in the service
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                setBypassDnd(true) // Bypass Do Not Disturb
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun buildAlarmNotification(context: Context): android.app.Notification {
        val intent = Intent(context, AlarmRingingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create Stop action
        val stopIntent = Intent(context, AlarmStopReceiver::class.java).apply {
            action = "com.medicinereminder.app.STOP_ALARM"
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Get scheduled time from preferences
        val prefs = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
        val scheduledTime = prefs.getLong("alarm_time_1", 0)
        val timeText = if (scheduledTime > 0) {
            val sdf = java.text.SimpleDateFormat("h:mm:ss a", java.util.Locale.getDefault())
            sdf.format(java.util.Date(scheduledTime))
        } else {
            "Now"
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("⏰ ALARM RINGING!")
            .setContentText("Scheduled for $timeText")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Time to take your medicine!\n\nScheduled: $timeText\n\nTap 'STOP' to dismiss the alarm."))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setFullScreenIntent(pendingIntent, true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                android.R.drawable.ic_delete,
                "STOP",
                stopPendingIntent
            )
        
        // Add vibration pattern for heads-up notification
        builder.setVibrate(longArrayOf(0, 500, 200, 500))
        
        return builder.build()
    }

    fun buildServiceNotification(context: Context): android.app.Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.alarm_service_notification))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    fun buildCountdownNotification(context: Context, remainingSeconds: Int): android.app.Notification {
        // Create cancel action
        val cancelIntent = Intent(context, CountdownService::class.java).apply {
            action = CountdownService.ACTION_CANCEL_ALARM
        }
        val cancelPendingIntent = PendingIntent.getService(
            context,
            2,
            cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val hours = remainingSeconds / 3600
        val minutes = (remainingSeconds % 3600) / 60
        val seconds = remainingSeconds % 60
        
        val timeText = if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else if (minutes > 0) {
            String.format("%d:%02d", minutes, seconds)
        } else {
            "${seconds}s"
        }

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("⏱️ Alarm Countdown")
            .setContentText("Alarm in $timeText")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Alarm will ring in $timeText\n\nTap CANCEL to stop the alarm."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setAutoCancel(false) // Cannot be dismissed by tapping
            .setOngoing(true) // Cannot be swiped away
            .setOnlyAlertOnce(true) // Don't make sound/vibration on updates
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "CANCEL",
                cancelPendingIntent
            )
            .build()
    }
}
