package com.umutsaydam.zenfocus.domain.usecases.remote.cases

import com.umutsaydam.zenfocus.domain.repository.remote.AuthRepository
import com.umutsaydam.zenfocus.util.AwsAuthSignInResult
import javax.inject.Inject

class AwsUserSignIn @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AwsAuthSignInResult {
        return authRepository.signIn(email, password)
    }
}