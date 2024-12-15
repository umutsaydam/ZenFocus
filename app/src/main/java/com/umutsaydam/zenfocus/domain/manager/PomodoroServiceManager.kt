package com.umutsaydam.zenfocus.domain.manager

interface PomodoroServiceManager {
    fun startPomodoroService()
    fun isRunning(): Boolean
    fun stopPomodoroService()
}