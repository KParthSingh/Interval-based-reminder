package com.medicinereminder.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.medicinereminder.app.ui.theme.MedicineReminderTheme

class AlarmRingingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ... (window flags code)
        
        setContent {
            MedicineReminderTheme {
                AlarmRingingScreen(
                    onDismiss = {
                        dismissAlarm()
                    }
                )
            }
        }
    }

    private fun dismissAlarm() {
        // Dismiss both notification IDs for safety (we now use CHAIN_NOTIFICATION_ID)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.cancel(NotificationHelper.NOTIFICATION_ID)
        notificationManager.cancel(NotificationHelper.CHAIN_NOTIFICATION_ID)
        
        val serviceIntent = Intent(this, AlarmService::class.java)
        stopService(serviceIntent)
        
        // DELEGATE NEXT STEP TO CHAIN SERVICE
        // ChainService will check isChainSequence() and stop if false.
        val nextIntent = Intent(this, ChainService::class.java).apply {
            action = ChainService.ACTION_NEXT_ALARM
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(nextIntent)
        } else {
            startService(nextIntent)
        }
        
        finish()
    }

    override fun onBackPressed() {
        // Prevent back button from dismissing alarm
    }
}

@Composable
fun AlarmRingingScreen(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6750A4), // Deep Purple
                        Color(0xFF21005D)  // Darker Purple
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            
            // Icon
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
               Icon(
                   Icons.Default.Notifications,
                   contentDescription = null,
                   tint = Color.White,
                   modifier = Modifier.size(64.dp)
               )
            }
            
            Spacer(modifier = Modifier.height(32.dp))

            // Text
            Text(
                text = stringResource(R.string.alarm_ringing),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = stringResource(R.string.alarm_ringing_subtitle),
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))
            
            // Dismiss Button
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                shape = RoundedCornerShape(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEADDFF),
                    contentColor = Color(0xFF21005D)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.dismiss_alarm).uppercase(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
