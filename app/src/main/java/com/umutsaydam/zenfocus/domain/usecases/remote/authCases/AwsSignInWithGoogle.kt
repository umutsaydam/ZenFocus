package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import android.app.Activity
import com.umutsaydam.zenfocus.domain.model.Resource
import com.umutsaydam.zenfocus.domain.repository.remote.AwsAuthRepository

class AwsSignInWithGoogle(
    private val awsAuthRepository: AwsAuthRepository
) {
    suspend operator fun invoke(activity: Activity): Resource<String> {
        return awsAuthRepository.signUpOrInWithGoogle(activity)
    }
}