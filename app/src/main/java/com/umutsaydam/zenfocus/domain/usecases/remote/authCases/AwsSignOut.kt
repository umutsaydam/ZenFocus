package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import com.umutsaydam.zenfocus.domain.repository.remote.AwsAuthRepository

class AwsSignOut(
    private val awsAuthRepository: AwsAuthRepository
) {
    suspend operator fun invoke(){
        awsAuthRepository.signOut()
    }
}