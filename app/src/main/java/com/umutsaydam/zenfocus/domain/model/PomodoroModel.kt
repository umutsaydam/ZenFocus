package com.umutsaydam.zenfocus.domain.model

import kotlin.time.Duration

data class PomodoroModel(
    val pomodoroCircle: Duration,
    val pomodoroWorkQuantity: Int,
    val pomodoroBreakQuantity: Int,
)
