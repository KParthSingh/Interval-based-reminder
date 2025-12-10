package com.medicinereminder.app

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

class AlarmService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("AlarmService", "Service created")

        // Create notification channel
        NotificationHelper.createNotificationChannel(this)

        // Get the current chain info from ChainManager
        val chainManager = ChainManager(this)
        val currentIndex = chainManager.getCurrentIndex()
        
        // Get alarm info from repository
        val alarmRepository = AlarmRepository(this)
        val alarms = alarmRepository.loadAlarms()
        val currentAlarmName = if (currentIndex < alarms.size) alarms[currentIndex].name else ""
        
        // Start as foreground service using the unified chain notification in alarm state
        val notification = NotificationHelper.buildChainNotification(
            this,
            currentIndex + 1,
            alarms.size,
            0, // remainingSeconds doesn't matter when alarm is ringing
            currentAlarmName,
            isPaused = false,
            isAlarmRinging = true
        )
        startForeground(NotificationHelper.CHAIN_NOTIFICATION_ID, notification)

        // Start playing alarm sound
        playAlarmSound()
        
        // Start vibration
        startVibration()
    }

    private fun playAlarmSound() {
        try {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            mediaPlayer = MediaPlayer().apply {
                setDataSource(applicationContext, alarmUri)
                
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
                
                setAudioAttributes(audioAttributes)
                isLooping = true
                prepare()
                start()
            }
            Log.d("AlarmService", "Alarm sound started")
        } catch (e: Exception) {
            Log.e("AlarmService", "Error playing alarm sound", e)
        }
    }

    private fun startVibration() {
        try {
            vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                getSystemService(VIBRATOR_SERVICE) as Vibrator
            }

            val pattern = longArrayOf(0, 500, 500) // Wait 0ms, vibrate 500ms, pause 500ms
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(
                    VibrationEffect.createWaveform(pattern, 0),
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern, 0)
            }
            Log.d("AlarmService", "Vibration started")
        } catch (e: Exception) {
            Log.e("AlarmService", "Error starting vibration", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("AlarmService", "Service destroyed")
        
        // Stop media player
        mediaPlayer?.apply {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = null

        // Stop vibration
        vibrator?.cancel()
        vibrator = null
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }
}
