package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import com.umutsaydam.zenfocus.domain.repository.remote.AwsAuthRepository
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignUpResult
import javax.inject.Inject

class AwsUserSignUpConfirm @Inject constructor(
    private val awsAuthRepository: AwsAuthRepository
) {
    suspend operator fun invoke(email: String, confirmCode: String): AwsAuthSignUpResult {
       return awsAuthRepository.confirmAccount(email, confirmCode)
    }
}