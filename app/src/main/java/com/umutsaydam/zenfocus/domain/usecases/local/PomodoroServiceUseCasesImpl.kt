package com.umutsaydam.zenfocus.domain.usecases.local

import com.umutsaydam.zenfocus.domain.manager.PomodoroServiceManager

interface PomodoroServiceUseCases {
    fun startService()
    fun isRunning(): Boolean
    fun stopService()
}

class PomodoroServiceUseCasesImpl(
    private val pomodoroServiceManager: PomodoroServiceManager
) : PomodoroServiceUseCases {

    override fun startService() {
        pomodoroServiceManager.startPomodoroService()
    }

    override fun isRunning(): Boolean {
        return pomodoroServiceManager.isRunning()
    }

    override fun stopService() {
        pomodoroServiceManager.stopPomodoroService()
    }
}