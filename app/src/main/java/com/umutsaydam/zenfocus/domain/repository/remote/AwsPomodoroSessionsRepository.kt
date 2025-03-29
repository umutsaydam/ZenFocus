package com.umutsaydam.zenfocus.domain.repository.remote

import com.umutsaydam.zenfocus.data.remote.dto.APIResponse
import com.umutsaydam.zenfocus.domain.model.PomodoroSessionModel
import com.umutsaydam.zenfocus.domain.model.Resource

interface AwsPomodoroSessionsRepository {
    suspend fun backupPomodoroSessions(userId: String, sessions: List<PomodoroSessionModel>) : Resource<APIResponse>
    suspend fun synchronizePomodoroSessions(userId: String) : Resource<Unit>
}