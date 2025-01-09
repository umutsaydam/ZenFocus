package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import com.umutsaydam.zenfocus.domain.service.AwsAuthService

class AwsSignOut(
    private val awsAuthService: AwsAuthService
) {
    suspend operator fun invoke(){
        awsAuthService.signOut()
    }
}