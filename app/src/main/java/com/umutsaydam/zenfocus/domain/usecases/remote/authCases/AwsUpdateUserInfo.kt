package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import com.umutsaydam.zenfocus.data.remote.dto.UserInfo
import com.umutsaydam.zenfocus.domain.model.Resource
import com.umutsaydam.zenfocus.domain.repository.remote.AwsAuthRepository

class AwsUpdateUserInfo(
    private val awsAuthRepository: AwsAuthRepository
) {
    suspend operator fun invoke(userId: String, userType: String): Resource<UserInfo> {
        return awsAuthRepository.updateUserType(userId, userType)
    }
}