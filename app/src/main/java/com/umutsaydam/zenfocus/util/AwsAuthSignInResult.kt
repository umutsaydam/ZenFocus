package com.umutsaydam.zenfocus.util

import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.result.step.AuthSignInStep

sealed class AwsAuthSignInResult {
    data class IsSignedIn(val isSignedIn: Boolean) : AwsAuthSignInResult()
    data class ConfirmSignIn(val nextStep: AuthSignInStep) : AwsAuthSignInResult()
    data class Error(val exception: AuthException) : AwsAuthSignInResult()
}