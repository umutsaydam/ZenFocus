package com.umutsaydam.zenfocus.domain.usecases.remote.cases

import com.umutsaydam.zenfocus.domain.repository.remote.AuthRepository
import com.umutsaydam.zenfocus.util.Resource
import javax.inject.Inject

class AwsUserGetUserId @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Resource<String> {
        return authRepository.getCurrentUserId()
    }
}