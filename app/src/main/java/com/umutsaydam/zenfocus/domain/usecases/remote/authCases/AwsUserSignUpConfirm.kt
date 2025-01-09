package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import com.umutsaydam.zenfocus.domain.model.AwsAuthSignUpResult
import com.umutsaydam.zenfocus.domain.service.AwsAuthService

class AwsUserSignUpConfirm(
    private val awsAuthService: AwsAuthService
) {
    suspend operator fun invoke(email: String, confirmCode: String): AwsAuthSignUpResult {
        return awsAuthService.confirmAccount(email, confirmCode)
    }
}