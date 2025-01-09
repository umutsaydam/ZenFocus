package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import com.umutsaydam.zenfocus.domain.model.AwsAuthSignUpResult
import com.umutsaydam.zenfocus.domain.service.AwsAuthService

class AwsUserSignUp(
    private val awsAuthService: AwsAuthService
) {
    suspend operator fun invoke(email: String, password: String): AwsAuthSignUpResult {
        return awsAuthService.signUp(email, password)
    }
}