package com.umutsaydam.zenfocus.domain.usecases.local

import com.umutsaydam.zenfocus.domain.manager.PomodoroManager
import kotlinx.coroutines.flow.StateFlow

interface PomodoroManagerUseCase {
    fun startPomodoro()
    fun pausePomodoro()
    fun resumePomodoro()
    fun stopPomodoro()
    fun setWorkCycle(workCycle: Int)
    fun setBreakDurationAsMinute(minute: Int)
    fun setWorkDurationAsMinute(minute: Int)
    fun getRemainingTimeMilli(): StateFlow<Long>
    fun getRemainingTimeAsTextFormat(): StateFlow<String>
    fun getRemainingPercent(): StateFlow<Float>
    fun isTimerRunning(): StateFlow<Boolean>
    fun setVibrateState(isEnabled: Boolean)
}

class PomodoroManagerUseCaseImpl(
    private val pomodoroManager: PomodoroManager
) : PomodoroManagerUseCase {
    override fun startPomodoro() {
        pomodoroManager.startTimer()
    }

    override fun pausePomodoro() {
        pomodoroManager.pauseTimer()
    }

    override fun resumePomodoro() {
        pomodoroManager.resumeTimer()
    }

    override fun stopPomodoro() {
        pomodoroManager.stopTimer()
    }

    override fun setWorkCycle(workCycle: Int) {
        pomodoroManager.setPomodoroWorkCycle(workCycle)
    }

    override fun setBreakDurationAsMinute(minute: Int) {
        pomodoroManager.setPomodoroBreakDurationAsMinute(minute)
    }

    override fun setWorkDurationAsMinute(minute: Int) {
        pomodoroManager.setPomodoroWorkDurationAsMinute(minute)
    }

    override fun getRemainingTimeMilli(): StateFlow<Long> {
        return pomodoroManager.remainingTimeMilli
    }

    override fun getRemainingTimeAsTextFormat(): StateFlow<String> {
        return pomodoroManager.remainingTimeText
    }

    override fun getRemainingPercent(): StateFlow<Float> {
        return pomodoroManager.remainingPercent
    }

    override fun isTimerRunning(): StateFlow<Boolean> {
        return pomodoroManager.isRunning()
    }

    override fun setVibrateState(isEnabled: Boolean) {
        pomodoroManager.setVibrateState(isEnabled)
    }
}