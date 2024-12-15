package com.umutsaydam.zenfocus.data.local.mapper

import android.media.AudioManager
import com.umutsaydam.zenfocus.domain.model.RingerModeEnum

object RingerModeMapper {
    fun mapFromAudioManager(ringMode: Int): RingerModeEnum {
        return when (ringMode) {
            AudioManager.RINGER_MODE_NORMAL -> RingerModeEnum.NORMAL
            AudioManager.RINGER_MODE_VIBRATE -> RingerModeEnum.VIBRATE
            AudioManager.RINGER_MODE_SILENT -> RingerModeEnum.SILENT
            else -> RingerModeEnum.SILENT
        }
    }
}