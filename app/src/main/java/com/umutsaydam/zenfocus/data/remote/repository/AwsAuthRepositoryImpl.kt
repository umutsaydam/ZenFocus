package com.umutsaydam.zenfocus.data.remote.repository

import android.app.Activity
import com.umutsaydam.zenfocus.data.remote.dto.UserInfo
import com.umutsaydam.zenfocus.domain.repository.remote.AwsAuthRepository
import com.umutsaydam.zenfocus.domain.service.AwsAuthService
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignInResult
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignUpResult
import com.umutsaydam.zenfocus.domain.model.Resource
import javax.inject.Inject

class AwsAuthRepositoryImpl @Inject constructor(
    private val awsAuthService: AwsAuthService
) : AwsAuthRepository {

    override suspend fun signIn(email: String, password: String): AwsAuthSignInResult {
        return awsAuthService.signIn(email, password)
    }

    override suspend fun signUp(email: String, password: String): AwsAuthSignUpResult {
        return awsAuthService.signUp(email, password)
    }

    override suspend fun confirmAccount(email: String, confirmCode: String): AwsAuthSignUpResult {
        return awsAuthService.confirmAccount(email, confirmCode)
    }

    override suspend fun resendConfirmationCode(email: String): AwsAuthSignUpResult {
        return awsAuthService.resendConfirmationCode(email)
    }

    override suspend fun getCurrentUserId(): Resource<String> {
        return awsAuthService.getCurrentUserId()
    }

    override suspend fun signUpOrInWithGoogle(activity: Activity): Resource<String> {
        return awsAuthService.signUpOrInWithGoogle(activity)
    }

    override suspend fun getUserInfo(userID: String): Resource<UserInfo> {
        return awsAuthService.getUserInfo(userID)
    }

    override suspend fun updateUserType(userId: String, userType: String): Resource<UserInfo> {
        return awsAuthService.updateUserType(userId, userType)
    }

    override suspend fun signOut() {
        awsAuthService.signOut()
    }
}