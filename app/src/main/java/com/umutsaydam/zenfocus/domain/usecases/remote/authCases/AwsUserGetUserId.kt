package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import com.umutsaydam.zenfocus.domain.model.Resource
import com.umutsaydam.zenfocus.domain.service.AwsAuthService

class AwsUserGetUserId(
    private val awsAuthService: AwsAuthService
) {
    suspend operator fun invoke(): Resource<String> {
        return awsAuthService.getCurrentUserId()
    }
}