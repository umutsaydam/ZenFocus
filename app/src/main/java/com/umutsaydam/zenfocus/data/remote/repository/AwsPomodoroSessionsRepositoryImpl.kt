package com.umutsaydam.zenfocus.data.remote.repository

import aws.smithy.kotlin.runtime.http.HttpStatusCode
import com.amplifyframework.api.rest.RestOptions
import com.amplifyframework.core.Amplify
import com.google.gson.Gson
import com.umutsaydam.zenfocus.data.remote.dto.APIResponse
import com.umutsaydam.zenfocus.data.remote.dto.PomodoroSessionsModel
import com.umutsaydam.zenfocus.domain.model.PomodoroSessionModel
import com.umutsaydam.zenfocus.domain.model.Resource
import com.umutsaydam.zenfocus.domain.repository.remote.AwsPomodoroSessionsRepository
import com.umutsaydam.zenfocus.domain.usecases.pomodoroSessions.PomodoroSessionsUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

class AwsPomodoroSessionsRepositoryImpl(
    private val pomodoroSessionsUseCases: PomodoroSessionsUseCases,
) : AwsPomodoroSessionsRepository {
    override suspend fun backupPomodoroSessions(
        userId: String,
        sessions: List<PomodoroSessionModel>
    ): Resource<APIResponse> {
        val data = mapOf(
            "sessionuserid" to userId,
            "sessions" to sessions.map { session ->
                mapOf(
                    "sessionId" to session.sessionId.toString(),
                    "breakDuration" to session.breakDuration,
                    "sessionDate" to session.sessionDate,
                    "workDuration" to session.workDuration
                )
            }
        )
        val jsonData = Gson().toJson(data)
        val requestBody = jsonData.toByteArray()

        return suspendCancellableCoroutine { continuation ->
            val request = RestOptions.builder()
                .addPath("/insertPomodoroSessions")
                .addBody(requestBody)
                .build()

            Amplify.API.post(
                request,
                { response ->
                    val responseData = response.data.asString()
                    val gson = Gson()
                    val apiResponse = gson.fromJson(responseData, APIResponse::class.java)
                    continuation.resume(Resource.Success(apiResponse)) { _, _, _ -> }
                },
                { failure ->
                    continuation.resume(Resource.Error(failure.message)) { _, _, _ -> }
                }
            )
        }
    }

    override suspend fun synchronizePomodoroSessions(userId: String): Resource<Unit> {
        val data = mapOf("sessionuserid" to userId)
        val jsonData = Gson().toJson(data)
        val requestBody = jsonData.toByteArray()

        return suspendCancellableCoroutine { continuation ->
            val request = RestOptions.builder()
                .addPath("/getPomodoroSessionsById")
                .addBody(requestBody)
                .build()

            Amplify.API.post(
                request,
                { response ->
                    val responseData = response.data.asString()

                    if (response.code.equals(HttpStatusCode.NotFound)) {
                        continuation.resume(Resource.Success(Unit)) { _, _, _ -> }
                    }

                    try {
                        val gson = Gson()
                        val remoteSessions =
                            gson.fromJson(responseData, PomodoroSessionsModel::class.java)

                        CoroutineScope(Dispatchers.IO).launch {
                            val localSessions = pomodoroSessionsUseCases.getAllSessions()
                            if (localSessions.isNotEmpty()) {
                                val newSessions = remoteSessions.sessions.filter { remote ->
                                    localSessions.none { local ->
                                        local.sessionId == remote.sessionId }
                                }
                                if (newSessions.isNotEmpty()) {
                                    pomodoroSessionsUseCases.insertAllSessions(newSessions)
                                }
                            } else {
                                pomodoroSessionsUseCases.insertAllSessions(remoteSessions.sessions)
                            }
                        }

                        continuation.resume(Resource.Success(Unit)) { _, _, _ -> }
                    } catch (e: Exception) {
                        continuation.resume(Resource.Error("JSON Parsing Error: ${e.message}")) { _, _, _ -> }
                    }
                },
                { failure ->
                    continuation.resume(
                        Resource.Error(
                            failure.message ?: "Unknown Error"
                        )
                    ) { _, _, _ -> }
                }
            )
        }
    }
}