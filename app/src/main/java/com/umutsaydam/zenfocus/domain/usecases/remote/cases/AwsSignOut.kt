package com.umutsaydam.zenfocus.domain.usecases.remote.cases

import com.umutsaydam.zenfocus.domain.repository.remote.AuthRepository

class AwsSignOut(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(){
        authRepository.signOut()
    }
}