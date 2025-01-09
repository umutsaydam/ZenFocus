package com.umutsaydam.zenfocus.domain.usecases.remote.authCases

import android.app.Activity
import com.umutsaydam.zenfocus.domain.model.Resource
import com.umutsaydam.zenfocus.domain.service.AwsAuthService

class AwsSignInWithGoogle(
    private val awsAuthService: AwsAuthService
) {
    suspend operator fun invoke(activity: Activity): Resource<String> {
        return awsAuthService.signUpOrInWithGoogle(activity)
    }
}