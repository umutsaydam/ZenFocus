package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import com.umutsaydam.zenfocus.data.remote.dto.UserInfo
import com.umutsaydam.zenfocus.domain.repository.remote.AwsAuthRepository
import com.umutsaydam.zenfocus.util.Resource

class AwsReadUserInfo(
    private val awsAuthRepository: AwsAuthRepository
) {
    suspend operator fun invoke(userId: String): Resource<UserInfo> {
        return awsAuthRepository.getUserInfo(userId)
    }
}