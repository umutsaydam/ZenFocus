package com.umutsaydam.zenfocus.domain.manager

import kotlinx.coroutines.flow.StateFlow

interface PomodoroManager {
    val remainingPercent: StateFlow<Float>
    val remainingTimeText: StateFlow<String>
    val isTimerRunning: StateFlow<Boolean>
    val workDuration: StateFlow<Long>
    val breakDuration: StateFlow<Long>
    val pomodoroWorkCycle: StateFlow<Int>
    val isWorkingSession: StateFlow<Boolean>

    fun startTimer()
    fun pauseTimer()
    fun resumeTimer()
    fun stopTimer()

    fun isRunning(): StateFlow<Boolean>

    fun setPomodoroTimeAsMinute(minute: Int)
    fun setPomodoroTimeAsMilli(milli: Long)

    fun setPomodoroWorkDurationAsMinute(minute: Int)
    fun setPomodoroBreakDurationAsMinute(minute: Int)

    fun setPomodoroWorkCycle(workCycle: Int)

    fun decreaseWorkCycle()

    fun switchBreakSession()
    fun switchWorkSession()

    fun pomodoroAvailableWorkCycle(): Boolean

    fun setVibrateState(isEnabled: Boolean)
    fun vibrateIfAvailable()

    fun playIfAvailable()

    fun notifyIfAvailable(soundResource: Int)
}