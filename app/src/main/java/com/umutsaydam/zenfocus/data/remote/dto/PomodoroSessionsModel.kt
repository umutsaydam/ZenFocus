package com.umutsaydam.zenfocus.data.remote.dto

import com.umutsaydam.zenfocus.domain.model.PomodoroSessionModel

data class PomodoroSessionsModel(
    val sessions: List<PomodoroSessionModel> = emptyList()
)
