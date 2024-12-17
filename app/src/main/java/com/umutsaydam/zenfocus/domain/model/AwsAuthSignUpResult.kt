package com.umutsaydam.zenfocus.domain.model

import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.result.step.AuthSignUpStep

sealed class AwsAuthSignUpResult {
    data class IsSignedUp(val isSignedUp: Boolean) : AwsAuthSignUpResult()
    data class ConfirmSignUp(val nextStep: AuthSignUpStep) : AwsAuthSignUpResult()
    data class Error(val exception: AuthException) : AwsAuthSignUpResult()
}