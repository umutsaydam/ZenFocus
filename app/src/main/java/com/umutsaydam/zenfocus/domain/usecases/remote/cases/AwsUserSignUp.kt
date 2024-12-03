package com.umutsaydam.zenfocus.domain.usecases.remote.cases

import com.umutsaydam.zenfocus.domain.repository.remote.AuthRepository
import com.umutsaydam.zenfocus.util.AwsAuthSignUpResult
import javax.inject.Inject

class AwsUserSignUp @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AwsAuthSignUpResult {
        return authRepository.signUp(email, password)
    }
}