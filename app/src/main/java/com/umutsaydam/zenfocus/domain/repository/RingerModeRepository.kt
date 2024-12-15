package com.umutsaydam.zenfocus.domain.repository

import com.umutsaydam.zenfocus.domain.model.RingerModeEnum

interface RingerModeRepository {
    fun getRingerMode(): RingerModeEnum
}