package com.umutsaydam.zenfocus.data.foregroundService

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
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
            PomodoroAction.ACTION_PAUSE.name -> {
                pauseTimer()
                updateNotification(remainingTimeText.value)
            }

            PomodoroAction.ACTION_RESUME.name -> {
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
            action = if (isTimerRunning) PomodoroAction.ACTION_PAUSE.name else PomodoroAction.ACTION_RESUME.name
        }
        val actionPendingIntent: PendingIntent = PendingIntent.getService(
            this,
            1,
            actionIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val actionIcon =
            if (isTimerRunning) R.drawable.ic_pause_white else R.drawable.ic_play_arrow_white
        val actionTitle = getString(if (isTimerRunning) R.string.pause else R.string.resume)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Pomodoro Timer")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_time)
            .setVibrate(longArrayOf(0))
            .setSilent(true)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(
                actionIcon,
                actionTitle,
                actionPendingIntent
            )
            .setAutoCancel(false)
            .build()
    }

    private fun observeRemainingTimeTextFormat() {
        notificationJob = CoroutineScope(Dispatchers.Default).launch {
            pomodoroManager.remainingTimeText.collect { remainingTimeTextFormat ->
                remainingTimeText.value = remainingTimeTextFormat
                updateNotification(remainingTimeTextFormat)
            }
        }
    }

    private fun updateNotification(newContentText: String) {
        if (isServiceRunning) {
            val notification = createNotification(newContentText)
            notificationManager.notify(notificationId, notification)
        } else {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanUpService()
    }
}

private enum class PomodoroAction {
    ACTION_PAUSE,
    ACTION_RESUME
}