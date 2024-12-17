package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import com.umutsaydam.zenfocus.domain.repository.remote.AwsAuthRepository
import com.umutsaydam.zenfocus.domain.model.Resource
import javax.inject.Inject

class AwsUserGetUserId @Inject constructor(
    private val awsAuthRepository: AwsAuthRepository
) {
    suspend operator fun invoke(): Resource<String> {
        return awsAuthRepository.getCurrentUserId()
    }
}