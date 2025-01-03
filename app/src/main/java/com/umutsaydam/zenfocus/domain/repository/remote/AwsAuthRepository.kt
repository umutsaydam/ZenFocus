package com.umutsaydam.zenfocus.domain.repository.remote

import android.app.Activity
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.umutsaydam.zenfocus.data.remote.dto.UserInfo
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignInResult
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignUpResult
import com.umutsaydam.zenfocus.domain.model.Resource

interface AwsAuthRepository {

    suspend fun signIn(email: String, password: String): AwsAuthSignInResult

    suspend fun signUp(email: String, password: String): AwsAuthSignUpResult

    suspend fun confirmAccount(email: String, confirmCode: String): AwsAuthSignUpResult

    suspend fun getCurrentUserId(): Resource<String>

    suspend fun signUpOrInWithGoogle(activity: Activity): Resource<String>

    suspend fun getUserInfo(userID: String): Resource<UserInfo>

    suspend fun updateUserType(userId: String, userType: String): Resource<UserInfo>

    suspend fun signOut()
}