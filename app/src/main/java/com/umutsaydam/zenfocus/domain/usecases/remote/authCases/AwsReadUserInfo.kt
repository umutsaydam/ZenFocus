package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import com.umutsaydam.zenfocus.data.remote.dto.UserInfo
import com.umutsaydam.zenfocus.domain.model.Resource
import com.umutsaydam.zenfocus.domain.service.AwsAuthService

class AwsReadUserInfo(
    private val awsAuthService: AwsAuthService
) {
    suspend operator fun invoke(userId: String): Resource<UserInfo> {
        return awsAuthService.getUserInfo(userId)
    }
}