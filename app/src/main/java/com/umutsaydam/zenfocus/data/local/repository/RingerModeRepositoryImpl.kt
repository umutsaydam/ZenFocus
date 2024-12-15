package com.umutsaydam.zenfocus.data.local.repository

import android.content.Context
import android.media.AudioManager
import com.umutsaydam.zenfocus.data.local.mapper.RingerModeMapper
import com.umutsaydam.zenfocus.domain.repository.RingerModeRepository
import com.umutsaydam.zenfocus.domain.model.RingerModeEnum
import dagger.hilt.android.qualifiers.ApplicationContext

class RingerModeRepositoryImpl(
    @ApplicationContext private val context: Context
) : RingerModeRepository {
    override fun getRingerMode(): RingerModeEnum {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return RingerModeMapper.mapFromAudioManager(audioManager.ringerMode)
    }
}