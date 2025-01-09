package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import com.umutsaydam.zenfocus.domain.model.AwsAuthSignInResult
import com.umutsaydam.zenfocus.domain.service.AwsAuthService

class AwsUserSignIn(
    private val awsAuthService: AwsAuthService
) {
    suspend operator fun invoke(email: String, password: String): AwsAuthSignInResult {
        return awsAuthService.signIn(email, password)
    }
}