package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import com.umutsaydam.zenfocus.domain.repository.remote.AwsAuthRepository
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignUpResult
import javax.inject.Inject

class AwsUserSignUp @Inject constructor(
    private val awsAuthRepository: AwsAuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AwsAuthSignUpResult {
        return awsAuthRepository.signUp(email, password)
    }
}