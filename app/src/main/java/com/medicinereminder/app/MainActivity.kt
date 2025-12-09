package com.medicinereminder.app

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private lateinit var alarmScheduler: AlarmScheduler
    private var hasNotificationPermission by mutableStateOf(true)

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        alarmScheduler = AlarmScheduler(this)
        
        // Create notification channel
        NotificationHelper.createNotificationChannel(this)
        
        // Check and request notification permission (Android 13+)
        checkNotificationPermission()
        
        // Check exact alarm permission
        checkExactAlarmPermission()

        setContent {
            MedicineReminderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        onScheduleAlarm = { delayMillis, requestCode ->
                            scheduleAlarm(delayMillis, requestCode)
                        }
                    )
                }
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasNotificationPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasNotificationPermission) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                // Guide user to settings
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.parse("package:$packageName")
                }
                startActivity(intent)
            }
        }
    }

    private fun scheduleAlarm(delayMillis: Long, requestCode: Int) {
        alarmScheduler.scheduleAlarm(delayMillis, requestCode)
        
        // Start countdown notification service
        val countdownIntent = Intent(this, CountdownService::class.java).apply {
            action = CountdownService.ACTION_START_COUNTDOWN
            putExtra(CountdownService.EXTRA_END_TIME, System.currentTimeMillis() + delayMillis)
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(countdownIntent)
        } else {
            startService(countdownIntent)
        }
    }
}

@Composable
fun MedicineReminderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC6),
            background = Color(0xFFF5F5F5)
        ),
        content = content
    )
}

@Composable
fun MainScreen(onScheduleAlarm: (Long, Int) -> Unit) {
    var alarmName by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf(0) }
    var minutes by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(5) } // Default 5 seconds
    var countdown by remember { mutableStateOf(0L) }
    var alarmScheduledTime by remember { mutableStateOf<Long?>(null) }

    // Countdown timer
    LaunchedEffect(countdown) {
        if (countdown > 0) {
            delay(1000)
            countdown -= 1000
        } else {
            alarmScheduledTime = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App title
        Text(
            text = "Medicine Reminder",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6200EE),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Main alarm card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Alarm name section
                Text(
                    text = "Alarm Name (Optional)",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = alarmName,
                    onValueChange = { alarmName = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g., First Medicine") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6200EE),
                        unfocusedBorderColor = Color.LightGray
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Time picker section
                Text(
                    text = "Set Time",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Clock-style time picker
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TimePickerColumn(
                        value = hours,
                        range = 0..23,
                        label = "Hours",
                        onValueChange = { hours = it }
                    )

                    Text(
                        text = ":",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6200EE),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    TimePickerColumn(
                        value = minutes,
                        range = 0..59,
                        label = "Min",
                        onValueChange = { minutes = it }
                    )

                    Text(
                        text = ":",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6200EE),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    TimePickerColumn(
                        value = seconds,
                        range = 0..59,
                        label = "Sec",
                        onValueChange = { seconds = it }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Quick presets
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { hours = 0; minutes = 0; seconds = 5 },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("5s", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { hours = 0; minutes = 0; seconds = 30 },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("30s", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { hours = 0; minutes = 1; seconds = 0 },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("1m", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { hours = 0; minutes = 5; seconds = 0 },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("5m", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Status card (when alarm is scheduled)
        if (countdown > 0) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF3E0)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⏱️",
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            if (alarmName.isNotBlank()) {
                                Text(
                                    text = alarmName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE65100)
                                )
                            }
                            val remainingSeconds = (countdown / 1000).toInt()
                            val h = remainingSeconds / 3600
                            val m = (remainingSeconds % 3600) / 60
                            val s = remainingSeconds % 60
                            Text(
                                text = "Alarm in ${String.format("%02d:%02d:%02d", h, m, s)}",
                                fontSize = 14.sp,
                                color = Color(0xFFE65100)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Start button
        val totalSeconds = hours * 3600 + minutes * 60 + seconds
        Button(
            onClick = {
                val delayMillis = totalSeconds * 1000L
                countdown = delayMillis
                alarmScheduledTime = System.currentTimeMillis() + delayMillis
                onScheduleAlarm(delayMillis, 1)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE)
            ),
            enabled = countdown == 0L && totalSeconds > 0
        ) {
            Text(
                text = "Start Alarm",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Info text
        Text(
            text = "💡 Alarms work in background, silent mode, and when screen is off",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun TimePickerColumn(
    value: Int,
    range: IntRange,
    label: String,
    onValueChange: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        // Up button
        IconButton(
            onClick = { 
                val newValue = if (value < range.last) value + 1 else range.first
                onValueChange(newValue)
            },
            modifier = Modifier.size(36.dp)
        ) {
            Text("▲", fontSize = 14.sp, color = Color(0xFF6200EE))
        }

        // Value display
        Text(
            text = String.format("%02d", value),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6200EE),
            textAlign = TextAlign.Center
        )

        // Down button
        IconButton(
            onClick = { 
                val newValue = if (value > range.first) value - 1 else range.last
                onValueChange(newValue)
            },
            modifier = Modifier.size(36.dp)
        ) {
            Text("▼", fontSize = 14.sp, color = Color(0xFF6200EE))
        }

        // Label
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray
        )
    }
}
