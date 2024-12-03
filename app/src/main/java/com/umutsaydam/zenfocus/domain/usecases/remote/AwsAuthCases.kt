package com.umutsaydam.zenfocus.domain.usecases.remote

import com.umutsaydam.zenfocus.domain.usecases.remote.cases.AwsReadUserInfo
import com.umutsaydam.zenfocus.domain.usecases.remote.cases.AwsSignOut
import com.umutsaydam.zenfocus.domain.usecases.remote.cases.AwsUserGetUserId
import com.umutsaydam.zenfocus.domain.usecases.remote.cases.AwsUserSignIn
import com.umutsaydam.zenfocus.domain.usecases.remote.cases.AwsUserSignUp
import com.umutsaydam.zenfocus.domain.usecases.remote.cases.AwsUserSignUpConfirm

data class AwsAuthCases(
    val userSignIn: AwsUserSignIn,
    val userSignUp: AwsUserSignUp,
    val userSignUpConfirm: AwsUserSignUpConfirm,
    val userGetId: AwsUserGetUserId,
    val readUserInfo: AwsReadUserInfo,
    val signOut: AwsSignOut
)
