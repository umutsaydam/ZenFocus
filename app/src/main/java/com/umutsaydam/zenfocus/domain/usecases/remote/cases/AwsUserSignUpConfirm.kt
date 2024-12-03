package com.umutsaydam.zenfocus.domain.usecases.remote.cases

import com.umutsaydam.zenfocus.domain.repository.remote.AuthRepository
import com.umutsaydam.zenfocus.util.AwsAuthSignUpResult
import javax.inject.Inject

class AwsUserSignUpConfirm @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, confirmCode: String): AwsAuthSignUpResult {
       return authRepository.confirmAccount(email, confirmCode)
    }
}