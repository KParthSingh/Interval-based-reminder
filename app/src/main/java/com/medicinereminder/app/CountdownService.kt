package com.medicinereminder.app

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class CountdownService : Service() {
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var countdownJob: Job? = null
    private var endTime: Long = 0

    override fun onCreate() {
        super.onCreate()
        Log.d("CountdownService", "Countdown service created")
        NotificationHelper.createNotificationChannel(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_COUNTDOWN -> {
                endTime = intent.getLongExtra(EXTRA_END_TIME, 0)
                startCountdown()
            }
            ACTION_CANCEL_ALARM -> {
                cancelAlarm()
                stopSelf()
            }
            ACTION_STOP_COUNTDOWN -> {
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    private fun startCountdown() {
        // Start as foreground service with initial notification
        val notification = NotificationHelper.buildCountdownNotification(
            this,
            getRemainingSeconds()
        )
        startForeground(NotificationHelper.COUNTDOWN_NOTIFICATION_ID, notification)

        // Start countdown updates
        countdownJob?.cancel()
        countdownJob = serviceScope.launch {
            while (isActive && System.currentTimeMillis() < endTime) {
                val remaining = getRemainingSeconds()
                if (remaining <= 0) break

                val notification = NotificationHelper.buildCountdownNotification(
                    this@CountdownService,
                    remaining
                )
                
                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(NotificationHelper.COUNTDOWN_NOTIFICATION_ID, notification)

                delay(1000) // Update every second
            }
            
            // Countdown finished, stop the service
            stopSelf()
        }
    }

    private fun getRemainingSeconds(): Int {
        val remaining = (endTime - System.currentTimeMillis()) / 1000
        return remaining.toInt().coerceAtLeast(0)
    }

    private fun cancelAlarm() {
        // Cancel the scheduled alarm
        val alarmScheduler = AlarmScheduler(this)
        alarmScheduler.cancelAlarm(1)
        
        Log.d("CountdownService", "Alarm cancelled from countdown notification")
    }

    override fun onDestroy() {
        super.onDestroy()
        countdownJob?.cancel()
        serviceScope.cancel()
        
        // Clear the countdown notification
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NotificationHelper.COUNTDOWN_NOTIFICATION_ID)
        
        Log.d("CountdownService", "Countdown service destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_START_COUNTDOWN = "com.medicinereminder.app.START_COUNTDOWN"
        const val ACTION_CANCEL_ALARM = "com.medicinereminder.app.CANCEL_ALARM"
        const val ACTION_STOP_COUNTDOWN = "com.medicinereminder.app.STOP_COUNTDOWN"
        const val EXTRA_END_TIME = "end_time"
    }
}
