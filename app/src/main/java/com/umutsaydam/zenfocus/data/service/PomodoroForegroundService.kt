package com.umutsaydam.zenfocus.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.umutsaydam.zenfocus.MainActivity
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.domain.manager.PomodoroManager
import com.umutsaydam.zenfocus.util.Constants.POMODORO_NOTIFICATION_ID
import com.umutsaydam.zenfocus.util.Constants.POMODORO_SERVICE_CHANNEL_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PomodoroForegroundService : Service() {
    @Inject
    lateinit var pomodoroManager: PomodoroManager
    private val remainingTimeText: MutableStateFlow<String> = MutableStateFlow("00:00")

    private val channelId = POMODORO_SERVICE_CHANNEL_ID
    private lateinit var notificationManager: NotificationManager
    private val notificationId = POMODORO_NOTIFICATION_ID
    private var notificationJob: Job? = null

    companion object {
        var isServiceRunning = false
    }

    override fun onCreate() {
        super.onCreate()
        isServiceRunning = true
        createNotificationChannel()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "ACTION_PAUSE" -> {
                Log.i("R/T", "Timer was paused")
                pauseTimer()
                updateNotification(remainingTimeText.value)
            }

            "ACTION_RESUME" -> {
                Log.i("R/T", "Timer is resuming...")
                resumeTimer()
            }

            else -> {
                observeRemainingTimeTextFormat()
                val notification = createNotification(remainingTimeText.value)
                startForeground(1, notification)
            }
        }

        return START_NOT_STICKY
    }

    private fun createNotification(contentText: String): Notification {
        val isTimerRunning = pomodoroManager.isRunning().value

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val actionIntent = Intent(this, PomodoroForegroundService::class.java).apply {
            action = if (isTimerRunning) "ACTION_PAUSE" else "ACTION_RESUME"
        }
        val actionPendingIntent: PendingIntent = PendingIntent.getService(
            this,
            1,
            actionIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val actionIcon =
            if (isTimerRunning) R.drawable.ic_pause_white else R.drawable.ic_play_arrow_white
        val actionTitle = if (isTimerRunning) "Pause" else "Resume"

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Pomodoro Timer")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_time)
            .setVibrate(null)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(
                actionIcon,
                actionTitle,
                actionPendingIntent
            )
            .setAutoCancel(true)
            .build()
    }

    private fun observeRemainingTimeTextFormat() {
        notificationJob = CoroutineScope(Dispatchers.Default).launch {
            pomodoroManager.remainingTimeText.collect { remainingTimeTextFormat ->
                Log.i("R/T", "remainingTimeTextFormat -> $remainingTimeTextFormat")
                remainingTimeText.value = remainingTimeTextFormat

                updateNotification(remainingTimeTextFormat)
            }
        }
    }

    private fun updateNotification(newContentText: String) {
        if (isServiceRunning){
            val notification = createNotification(newContentText)
            notificationManager.notify(notificationId, notification)
        }else{
            cleanUpService()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                "Pomodoro Timer Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableVibration(false)
                vibrationPattern = longArrayOf(0L)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun resumeTimer() {
        pomodoroManager.resumeTimer()
        observeRemainingTimeTextFormat()
    }

    private fun pauseTimer() {
        pomodoroManager.pauseTimer()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun cleanUpService() {
        isServiceRunning = false
        notificationJob?.cancel()
        notificationJob = null
        stopSelf()
        stopForeground(STOP_FOREGROUND_REMOVE)
        notificationManager.cancel(notificationId)
        Log.i(
            "R/T",
            "Service stopped and notification removed ************* ${Build.VERSION.SDK_INT} *************"
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanUpService()
    }
}