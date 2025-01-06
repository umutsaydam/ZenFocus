package com.umutsaydam.zenfocus.domain.service

import android.app.Activity
import com.umutsaydam.zenfocus.data.remote.dto.UserInfo
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignInResult
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignUpResult
import com.umutsaydam.zenfocus.domain.model.Resource

interface AwsAuthService {
    suspend fun signIn(email: String, password: String): AwsAuthSignInResult

    suspend fun signUp(email: String, password: String): AwsAuthSignUpResult

    suspend fun confirmAccount(email: String, confirmCode: String): AwsAuthSignUpResult

    suspend fun resendConfirmationCode(email: String): AwsAuthSignUpResult

    suspend fun getCurrentUserId(): Resource<String>

    suspend fun getUserInfo(userId: String): Resource<UserInfo>

    suspend fun updateUserType(userId: String, userType: String): Resource<UserInfo>

    suspend fun fetchAuthSession(): Resource<String>

    suspend fun signUpOrInWithGoogle(activity: Activity): Resource<String>

    suspend fun signOut()
}