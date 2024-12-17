package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import com.umutsaydam.zenfocus.domain.repository.remote.AwsAuthRepository
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignInResult
import javax.inject.Inject

class AwsUserSignIn @Inject constructor(
    private val awsAuthRepository: AwsAuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AwsAuthSignInResult {
        return awsAuthRepository.signIn(email, password)
    }
}