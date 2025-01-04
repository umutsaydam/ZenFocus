package com.umutsaydam.zenfocus.data.local.manager

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.umutsaydam.zenfocus.data.service.PomodoroForegroundService
import com.umutsaydam.zenfocus.domain.manager.PomodoroServiceManager

class PomodoroServiceManagerImpl(
    private val context: Context
) : PomodoroServiceManager {

    override fun startPomodoroService() {
        if (!PomodoroForegroundService.isServiceRunning) {
            Log.i("R/T", "Service is starting ...")
            val intent = Intent(context, PomodoroForegroundService::class.java)
            ContextCompat.startForegroundService(context, intent)
        } else {
            Log.i("R/T", "Service is already running.")
        }
    }

    override fun isRunning(): Boolean {
        return PomodoroForegroundService.isServiceRunning
    }

    override fun stopPomodoroService() {
        if (PomodoroForegroundService.isServiceRunning){
            Log.i("R/T", "Service is stopping...")
            val intent = Intent(context, PomodoroForegroundService::class.java)
            context.stopService(intent)
        }else{
            Log.i("R/T", "Service is not working so can not stop.")
        }
    }
}