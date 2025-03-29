package com.umutsaydam.zenfocus.domain.usecases.remote

import com.umutsaydam.zenfocus.data.remote.dto.APIResponse
import com.umutsaydam.zenfocus.domain.model.PomodoroSessionModel
import com.umutsaydam.zenfocus.domain.model.Resource
import com.umutsaydam.zenfocus.domain.repository.remote.AwsPomodoroSessionsRepository

interface AwsPomodoroSessionsUseCases {
    suspend fun backupPomodoroSessions(
        userId: String,
        sessions: List<PomodoroSessionModel>
    ): Resource<APIResponse>
    suspend fun synchronizePomodoroSessions(userId: String): Resource<Unit>
}

class AwsPomodoroSessionsUseCasesImpl(
    private val sessionsRepository: AwsPomodoroSessionsRepository
) : AwsPomodoroSessionsUseCases {
    override suspend fun backupPomodoroSessions(
        userId: String,
        sessions: List<PomodoroSessionModel>
    ): Resource<APIResponse> {
        return sessionsRepository.backupPomodoroSessions(userId, sessions)
    }

    override suspend fun synchronizePomodoroSessions(userId: String): Resource<Unit> {
        return sessionsRepository.synchronizePomodoroSessions(userId)
    }
}