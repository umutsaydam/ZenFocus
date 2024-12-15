package com.umutsaydam.zenfocus.domain.usecases.local.cases.ringerModeCases

import com.umutsaydam.zenfocus.domain.repository.RingerModeRepository
import com.umutsaydam.zenfocus.domain.model.RingerModeEnum

class ReadRingerMode(
    private val ringerModeRepository: RingerModeRepository
) {
    operator fun invoke(): RingerModeEnum {
        return ringerModeRepository.getRingerMode()
    }
}