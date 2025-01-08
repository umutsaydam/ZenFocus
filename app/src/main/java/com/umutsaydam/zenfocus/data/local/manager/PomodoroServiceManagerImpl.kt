package com.umutsaydam.zenfocus.data.local.manager

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.umutsaydam.zenfocus.data.foregroundService.PomodoroForegroundService
import com.umutsaydam.zenfocus.domain.manager.PomodoroServiceManager

class PomodoroServiceManagerImpl(
    private val context: Context
) : PomodoroServiceManager {

    override fun startPomodoroService() {
        if (!PomodoroForegroundService.isServiceRunning) {
            val intent = Intent(context, PomodoroForegroundService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }
    }

    override fun isRunning(): Boolean {
        return PomodoroForegroundService.isServiceRunning
    }

    override fun stopPomodoroService() {
        if (PomodoroForegroundService.isServiceRunning) {
            val intent = Intent(context, PomodoroForegroundService::class.java)
            context.stopService(intent)
        }
    }
}