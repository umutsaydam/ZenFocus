package com.umutsaydam.zenfocus.data.remote

import com.umutsaydam.zenfocus.data.remote.dto.UserInfo
import com.umutsaydam.zenfocus.domain.repository.remote.AuthRepository
import com.umutsaydam.zenfocus.domain.service.AwsService
import com.umutsaydam.zenfocus.util.AwsAuthSignInResult
import com.umutsaydam.zenfocus.util.AwsAuthSignUpResult
import com.umutsaydam.zenfocus.util.Resource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val awsService: AwsService
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): AwsAuthSignInResult {
        return awsService.signIn(email, password)
    }

    override suspend fun signUp(email: String, password: String): AwsAuthSignUpResult {
        return awsService.signUp(email, password)
    }

    override suspend fun confirmAccount(email: String, confirmCode: String): AwsAuthSignUpResult {
        return awsService.confirmAccount(email, confirmCode)
    }

    override suspend fun getCurrentUserId(): Resource<String> {
        return awsService.getCurrentUserId()
    }

    override fun signUpOrInWithGoogle() {
        TODO("Not yet implemented")
    }

    override suspend fun getUserInfo(userID: String): Resource<UserInfo> {
        return awsService.getUserInfo(userID)
    }

    override suspend fun signOut() {
        awsService.signOut()
    }
}