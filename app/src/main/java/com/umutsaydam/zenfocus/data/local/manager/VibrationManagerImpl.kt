package com.umutsaydam.zenfocus.data.local.manager

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import com.umutsaydam.zenfocus.domain.manager.VibrationManager
import com.umutsaydam.zenfocus.domain.model.RingerModeEnum
import com.umutsaydam.zenfocus.domain.usecases.local.DeviceRingerModeCases
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class VibrationManagerImpl @Inject constructor(
    private val ringerModeCases: DeviceRingerModeCases,
    @ApplicationContext private val context: Context
) : VibrationManager {
    private var vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vbManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vbManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun vibrate(duration: Long) {
        if (vibrator.hasVibrator() && deviceVibrateModeAvailable()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect =
                    VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
                Log.i("R/T", "vibrating...")
                vibrator.vibrate(effect)
            } else {
                Log.i("R/T", "vibrating...1")
                @Suppress("DEPRECATION")
                vibrator.vibrate(duration)
            }
        } else {
            Log.i(
                "R/T",
                "Device Does not support vibration or device mode (etc. normal, vibrate, silent) is not available for vibration."
            )
        }
    }

    override fun deviceVibrateModeAvailable(): Boolean {
        return ringerModeCases.readRingerMode() in arrayOf(RingerModeEnum.NORMAL, RingerModeEnum.VIBRATE)
    }
}