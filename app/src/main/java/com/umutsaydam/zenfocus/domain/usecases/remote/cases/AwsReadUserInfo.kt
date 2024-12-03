package com.umutsaydam.zenfocus.domain.usecases.remote.cases

import com.umutsaydam.zenfocus.data.remote.dto.UserInfo
import com.umutsaydam.zenfocus.domain.repository.remote.AuthRepository
import com.umutsaydam.zenfocus.util.Resource

class AwsReadUserInfo(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userId: String): Resource<UserInfo> {
        return authRepository.getUserInfo(userId)
    }
}