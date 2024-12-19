package com.umutsaydam.zenfocus.domain.usecases.remote

import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsReadUserInfo
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsSignInWithGoogle
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsSignOut
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserGetUserId
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserSignIn
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserSignUp
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserSignUpConfirm

data class AwsAuthCases(
    val userSignIn: AwsUserSignIn,
    val userSignUp: AwsUserSignUp,
    val userSignUpConfirm: AwsUserSignUpConfirm,
    val userGetId: AwsUserGetUserId,
    val readUserInfo: AwsReadUserInfo,
    val signInWithGoogle: AwsSignInWithGoogle,
    val signOut: AwsSignOut
)
