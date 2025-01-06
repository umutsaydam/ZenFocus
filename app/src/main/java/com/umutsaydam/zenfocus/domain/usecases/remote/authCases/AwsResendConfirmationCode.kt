package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import com.umutsaydam.zenfocus.domain.model.AwsAuthSignUpResult
import com.umutsaydam.zenfocus.domain.repository.remote.AwsAuthRepository
import javax.inject.Inject

class AwsResendConfirmationCode  @Inject constructor(
    private val awsAuthRepository: AwsAuthRepository
) {
    suspend operator fun invoke(email: String): AwsAuthSignUpResult {
        return awsAuthRepository.resendConfirmationCode(email)
    }
}