package com.umutsaydam.zenfocus.data.local.manager

import android.os.CountDownTimer
import android.util.Log
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.domain.manager.FocusSoundManager
import com.umutsaydam.zenfocus.domain.manager.PomodoroManager
import com.umutsaydam.zenfocus.domain.manager.TimeOutRingerManager
import com.umutsaydam.zenfocus.domain.manager.VibrationManager
import com.umutsaydam.zenfocus.util.Constants.DEFAULT_VIBRATION_DURATION
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PomodoroManagerImpl(
    private val focusSoundManager: FocusSoundManager,
    private val timeOutRingerManager: TimeOutRingerManager,
    private val vibrationManager: VibrationManager
) : PomodoroManager {
    private var _initTime = 0L
    private val _remainingTime = MutableStateFlow<Long>(0)

    private val _remainingPercent = MutableStateFlow<Float>(0f)
    override val remainingPercent: StateFlow<Float> = _remainingPercent

    private val _remainingTimeText = MutableStateFlow<String>("00:00")
    override val remainingTimeText: StateFlow<String> = _remainingTimeText

    private lateinit var timer: CountDownTimer

    private val _isTimerRunning = MutableStateFlow(false)
    override val isTimerRunning: StateFlow<Boolean> = _isTimerRunning

    private val _workDuration = MutableStateFlow<Long>(0L)
    override val workDuration: StateFlow<Long> = _workDuration

    private val _breakDuration = MutableStateFlow<Long>(0L)
    override val breakDuration: StateFlow<Long> = _breakDuration

    private val _pomodoroWorkCycle = MutableStateFlow<Int>(0)
    override val pomodoroWorkCycle: StateFlow<Int> = _pomodoroWorkCycle

    private val _isWorkingSession = MutableStateFlow<Boolean>(true)
    override val isWorkingSession: StateFlow<Boolean> = _isWorkingSession

    private val _isVibrateEnabled = MutableStateFlow<Boolean>(true)
    override val isVibrateEnabled: StateFlow<Boolean> = _isVibrateEnabled

    override fun setPomodoroTimeAsMinute(minute: Int) {
        Log.i("R/T", "setTime was started as minute")
        val convertedTime = TimeConverter.minuteToMilli(minute)
        _remainingTime.value = convertedTime
        _initTime = convertedTime
    }

    override fun setPomodoroTimeAsMilli(milli: Long) {
        Log.i("R/T", "setTime was started as milli")
        _remainingTime.value = milli
        _initTime = milli
    }

    override fun setPomodoroWorkDurationAsMinute(minute: Int) {
        Log.i("R/T", "work duration set as $minute (seconds)")
        val convertedTime = TimeConverter.minuteToMilli(minute)
        _workDuration.value = convertedTime
    }

    override fun setPomodoroBreakDurationAsMinute(minute: Int) {
        Log.i("R/T", "break duration set as $minute (seconds)")
        val convertedTime = TimeConverter.minuteToMilli(minute)
        _breakDuration.value = convertedTime
    }

    override fun setPomodoroWorkCycle(workCycle: Int) {
        Log.i("R/T", "pomodoro work cycle set as $workCycle")
        _pomodoroWorkCycle.value = workCycle
    }

    override fun startTimer() {
        Log.i("R/T", "startTimer was started")
        if (!_isTimerRunning.value && _remainingTime.value == 0L) {
            if (_isWorkingSession.value) {
                Log.i("R/T", "pomodoro time set work duration")
                setPomodoroTimeAsMilli(_workDuration.value)
            } else {
                Log.i("R/T", "pomodoro time set break duration")
                setPomodoroTimeAsMilli(_breakDuration.value)
            }
        }

        if (_remainingTime.value > 0) {
            if (_isTimerRunning.value) {
                Log.i("R/T", "timer has been init so the old timer was canceled.")
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
                    Log.i("R/T", remainingTextFormat)

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
                            Log.i("R/T", "********* Pomodoro is completely complete *********")
                            stopTimer()
                            vibrateIfAvailable()
                            timeOutRingerManager.playSound(R.raw.completed_all_sessions)
                            Log.i("R/T", "timer finished")
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
        Log.i("R/t", "Switching break session...")
        stopTimer()
        focusSoundManager.stopSound()
        notifyIfAvailable(R.raw.start_break_session)
        _isWorkingSession.value = false
        setPomodoroTimeAsMilli(_breakDuration.value)
        startTimer()
    }

    override fun switchWorkSession() {
        Log.i("R/t", "Switching work session...")
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
        _isVibrateEnabled.value = isEnabled
    }

    override fun vibrateIfAvailable() {
        if (_isVibrateEnabled.value) {
            vibrationManager.vibrate(DEFAULT_VIBRATION_DURATION)
        }
    }

    override fun notifyIfAvailable(soundResource: Int) {
        timeOutRingerManager.playSound(soundResource)
    }

    override fun pauseTimer() {
        if (_isTimerRunning.value) {
            Log.i("R/T", "Pause Timer func started...")
            _isTimerRunning.value = false
            focusSoundManager.stopSound()
            timer.cancel()
            Log.i("R/T", "${_remainingTime.value}")
        } else {
            Log.i("R/T", "Pause can not stop because it is not working.")
        }
    }

    override fun resumeTimer() {
        if (!_isTimerRunning.value && _remainingTime.value > 0) {
            startTimer()
        }
    }

    override fun stopTimer() {
        if (_isTimerRunning.value) {
            Log.i("R/T", "timer stopped")
            _isTimerRunning.value = false
            focusSoundManager.stopSound()
            timer.cancel()
        } else {
            Log.i("R/T", "timer could not stopped. It is not initialized!")
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