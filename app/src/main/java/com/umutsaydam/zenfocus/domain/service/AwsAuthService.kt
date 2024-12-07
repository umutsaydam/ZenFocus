package com.umutsaydam.zenfocus.domain.service

import com.umutsaydam.zenfocus.data.remote.dto.UserInfo
import com.umutsaydam.zenfocus.util.AwsAuthSignInResult
import com.umutsaydam.zenfocus.util.AwsAuthSignUpResult
import com.umutsaydam.zenfocus.util.Resource

interface AwsAuthService {
    suspend fun signIn(email: String, password: String): AwsAuthSignInResult

    suspend fun signUp(email: String, password: String): AwsAuthSignUpResult

    suspend fun confirmAccount(email: String, confirmCode: String): AwsAuthSignUpResult

    suspend fun getCurrentUserId(): Resource<String>

    suspend fun getUserInfo(userId: String): Resource<UserInfo>

    suspend fun signOut()
}