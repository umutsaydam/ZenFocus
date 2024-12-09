package com.umutsaydam.zenfocus.data.remote.repository

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.umutsaydam.zenfocus.data.remote.dto.UserInfo
import com.umutsaydam.zenfocus.domain.repository.remote.AwsAuthRepository
import com.umutsaydam.zenfocus.domain.service.AwsAuthService
import com.umutsaydam.zenfocus.util.AwsAuthSignInResult
import com.umutsaydam.zenfocus.util.AwsAuthSignUpResult
import com.umutsaydam.zenfocus.util.Resource
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

    override suspend fun getCurrentUserId(): Resource<String> {
        return awsAuthService.getCurrentUserId()
    }

    override suspend fun signUpOrInWithGoogle(): Resource<AWSCognitoAuthSession> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserInfo(userID: String): Resource<UserInfo> {
        return awsAuthService.getUserInfo(userID)
    }

    override suspend fun signOut() {
        awsAuthService.signOut()
    }
}