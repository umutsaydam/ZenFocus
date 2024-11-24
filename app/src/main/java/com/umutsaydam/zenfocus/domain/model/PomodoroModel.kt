package com.umutsaydam.zenfocus.domain.model

data class PomodoroModel(
    val pomodoroCircle: Int,
    val pomodoroWorkQuantity: Int,
    val pomodoroBreakQuantity: Int,
    val themeModel: ThemeModel,
    val soundModel: SoundModel
)
