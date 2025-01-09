package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import com.umutsaydam.zenfocus.data.remote.dto.UserInfo
import com.umutsaydam.zenfocus.domain.model.Resource
import com.umutsaydam.zenfocus.domain.service.AwsAuthService

class AwsUpdateUserInfo(
    private val awsAuthService: AwsAuthService
) {
    suspend operator fun invoke(userId: String, userType: String): Resource<UserInfo> {
        return awsAuthService.updateUserType(userId, userType)
    }
}