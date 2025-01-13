package com.umutsaydam.zenfocus.data.local.manager

import android.os.CountDownTimer
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.domain.manager.FocusSoundManager
import com.umutsaydam.zenfocus.domain.manager.PomodoroManager
import com.umutsaydam.zenfocus.domain.usecases.local.TimeOutRingerManagerUseCases
import com.umutsaydam.zenfocus.domain.usecases.local.VibrationManagerUseCases
import com.umutsaydam.zenfocus.util.Constants.DEFAULT_VIBRATION_DURATION
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PomodoroManagerImpl(
    private val focusSoundManager: FocusSoundManager,
    private val timeOutRingerManagerUseCases: TimeOutRingerManagerUseCases,
    private val vibrationManagerUseCases: VibrationManagerUseCases
) : PomodoroManager {
    private var _initTime = 0L
    private val _remainingTime = MutableStateFlow<Long>(0)

    private val _remainingPercent = MutableStateFlow(0f)
    override val remainingPercent: StateFlow<Float> = _remainingPercent

    private val _remainingTimeText = MutableStateFlow("00:00")
    override val remainingTimeText: StateFlow<String> = _remainingTimeText

    private lateinit var timer: CountDownTimer

    private val _isTimerRunning = MutableStateFlow(false)
    override val isTimerRunning: StateFlow<Boolean> = _isTimerRunning

    private val _workDuration = MutableStateFlow(0L)
    override val workDuration: StateFlow<Long> = _workDuration

    private val _breakDuration = MutableStateFlow(0L)
    override val breakDuration: StateFlow<Long> = _breakDuration

    private val _pomodoroWorkCycle = MutableStateFlow(0)
    override val pomodoroWorkCycle: StateFlow<Int> = _pomodoroWorkCycle

    private val _isWorkingSession = MutableStateFlow(true)
    override val isWorkingSession: StateFlow<Boolean> = _isWorkingSession

    override fun setPomodoroTimeAsMinute(minute: Int) {
        val convertedTime = TimeConverter.minuteToMilli(minute)
        _remainingTime.value = convertedTime
        _initTime = convertedTime
    }

    override fun setPomodoroTimeAsMilli(milli: Long) {
        _remainingTime.value = milli
        _initTime = milli
    }

    override fun setPomodoroWorkDurationAsMinute(minute: Int) {
        val convertedTime = TimeConverter.minuteToMilli(minute)
        _workDuration.value = convertedTime
    }

    override fun setPomodoroBreakDurationAsMinute(minute: Int) {
        val convertedTime = TimeConverter.minuteToMilli(minute)
        _breakDuration.value = convertedTime
    }

    override fun setPomodoroWorkCycle(workCycle: Int) {
        _pomodoroWorkCycle.value = workCycle
    }

    override fun startTimer() {
        if (!_isTimerRunning.value && _remainingTime.value == 0L) {
            if (_isWorkingSession.value) {
                setPomodoroTimeAsMilli(_workDuration.value)
            } else {
                setPomodoroTimeAsMilli(_breakDuration.value)
            }
        }

        if (_remainingTime.value > 0) {
            if (_isTimerRunning.value) {
                timer.cancel()
            }

            if (_isWorkingSession.value) {
                focusSoundManager.playSoundIfAvailable()
            }
            _isTimerRunning.value = true
            timer = object : CountDownTimer(_remainingTime.value, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val divided = millisUntilFinished / 1000
                    val minutes = divided / 60
                    val seconds = divided % 60
                    val remainingTextFormat =
                        TimeConverter.convertMinutesAndSecondsToTextFormat(minutes, seconds)

                    _remainingTimeText.value = remainingTextFormat
                    _remainingTime.value = millisUntilFinished
                    _remainingPercent.value = millisUntilFinished.toFloat() / _initTime.toFloat()
                }

                override fun onFinish() {
                    _remainingTime.value = 0
                    _remainingPercent.value = 0f
                    _remainingTimeText.value = "00:00"

                    if (_isWorkingSession.value) {
                        decreaseWorkCycle()

                        if (pomodoroAvailableWorkCycle()) {
                            vibrateIfAvailable()
                            switchBreakSession()
                        } else {
                            // Pomodoro is completely complete.
                            stopTimer()
                            vibrateIfAvailable()
                            playIfAvailable()
                        }
                    } else {
                        vibrateIfAvailable()
                        switchWorkSession()
                    }
                }
            }.start()
        }
    }

    override fun decreaseWorkCycle() {
        if (pomodoroAvailableWorkCycle()) {
            _pomodoroWorkCycle.value--
        }
    }

    override fun switchBreakSession() {
        stopTimer()
        focusSoundManager.stopSound()
        notifyIfAvailable(R.raw.start_break_session)
        _isWorkingSession.value = false
        setPomodoroTimeAsMilli(_breakDuration.value)
        startTimer()
    }

    override fun switchWorkSession() {
        stopTimer()
        focusSoundManager.playSoundIfAvailable()
        notifyIfAvailable(R.raw.start_work_session)
        _isWorkingSession.value = true
        setPomodoroTimeAsMilli(_workDuration.value)
        startTimer()
    }

    override fun pomodoroAvailableWorkCycle(): Boolean {
        return _pomodoroWorkCycle.value > 0
    }

    override fun setVibrateState(isEnabled: Boolean) {
        vibrationManagerUseCases.setVibrateState(isEnabled)
    }

    override fun vibrateIfAvailable() {
        vibrationManagerUseCases.vibrate(DEFAULT_VIBRATION_DURATION)
    }

    override fun playIfAvailable() {
        timeOutRingerManagerUseCases.playSound(R.raw.completed_all_sessions)
    }

    override fun notifyIfAvailable(soundResource: Int) {
        timeOutRingerManagerUseCases.playSound(soundResource)
    }

    override fun pauseTimer() {
        if (_isTimerRunning.value) {
            _isTimerRunning.value = false
            focusSoundManager.stopSound()
            timer.cancel()
        }
    }

    override fun resumeTimer() {
        if (!_isTimerRunning.value && _remainingTime.value > 0) {
            startTimer()
        }
    }

    override fun stopTimer() {
        if (_isTimerRunning.value) {
            _isTimerRunning.value = false
            focusSoundManager.stopSound()
            timer.cancel()
            _remainingTime.value = 0
            _remainingPercent.value = 0f
            _remainingTimeText.value = "00:00"
        }
    }

    override fun isRunning(): StateFlow<Boolean> {
        return _isTimerRunning
    }
}

private object TimeConverter {
    fun minuteToMilli(minute: Int): Long {
//        return minute * 60 * 1000L
        return minute * 1000L
    }

    fun convertMinutesAndSecondsToTextFormat(minutes: Long, seconds: Long): String {
        return "%02d:%02d".format(minutes, seconds)
    }
}