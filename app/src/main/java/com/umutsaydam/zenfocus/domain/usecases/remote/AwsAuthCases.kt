package com.umutsaydam.zenfocus.domain.usecases.remote

import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsReadUserInfo
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsSignInWithGoogle
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsSignOut
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUpdateUserInfo
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserGetUserId
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserSignIn
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserSignUp
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsUserSignUpConfirm
import com.umutsaydam.zenfocus.domain.usecases.remote.authCases.AwsResendConfirmationCode

data class AwsAuthCases(
    val userSignIn: AwsUserSignIn,
    val userSignUp: AwsUserSignUp,
    val userSignUpConfirm: AwsUserSignUpConfirm,
    val awsResendConfirmationCode: AwsResendConfirmationCode,
    val userGetId: AwsUserGetUserId,
    val readUserInfo: AwsReadUserInfo,
    val updateUserInfo: AwsUpdateUserInfo,
    val signInWithGoogle: AwsSignInWithGoogle,
    val signOut: AwsSignOut
)
