package com.umutsaydam.zenfocus.data.remote.service

import android.app.Activity
import com.amplifyframework.api.rest.RestOptions
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.step.AuthSignInStep
import com.amplifyframework.auth.result.step.AuthSignUpStep
import com.amplifyframework.core.Amplify
import com.google.gson.Gson
import com.umutsaydam.zenfocus.data.remote.dto.UserInfo
import com.umutsaydam.zenfocus.domain.model.AuthExceptionMessages
import com.umutsaydam.zenfocus.domain.service.AwsAuthService
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignInResult
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignUpResult
import com.umutsaydam.zenfocus.domain.model.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

class AwsAuthServiceImpl : AwsAuthService {

    override suspend fun signIn(email: String, password: String): AwsAuthSignInResult {
        return suspendCancellableCoroutine { continuation ->
            Amplify.Auth.signIn(email, password, { result ->
                when {
                    result.isSignedIn -> {
                        continuation.resume(AwsAuthSignInResult.IsSignedIn(result.isSignedIn)) { _, _, _ -> }
                    }

                    result.nextStep.signInStep == AuthSignInStep.CONFIRM_SIGN_UP -> {
                        continuation.resume(AwsAuthSignInResult.ConfirmSignIn(result.nextStep.signInStep)) { _, _, _ -> }
                    }
                }
            }, { error ->
                val errorMessage = AuthExceptionMessages.exceptionToMessage(error)
                continuation.resume(
                    AwsAuthSignInResult.Error(
                        exception = error,
                        message = errorMessage.messageId
                    )
                ) { _, _, _ -> }
            })
        }
    }

    override suspend fun signUp(email: String, password: String): AwsAuthSignUpResult {
        return suspendCancellableCoroutine { continuation ->
            val options = AuthSignUpOptions.builder().userAttributes(
                listOf(
                    AuthUserAttribute(AuthUserAttributeKey.email(), email)
                )
            ).build()

            Amplify.Auth.signUp(email, password, options, { result ->
                when {
                    result.isSignUpComplete -> {
                        continuation.resume(AwsAuthSignUpResult.IsSignedUp(result.isSignUpComplete)) { _, _, _ -> }
                    }

                    result.nextStep.signUpStep == AuthSignUpStep.CONFIRM_SIGN_UP_STEP -> {
                        continuation.resume(AwsAuthSignUpResult.ConfirmSignUp(AuthSignUpStep.CONFIRM_SIGN_UP_STEP)) { _, _, _ -> }
                    }
                }
            }, { error ->
                val errorMessage = AuthExceptionMessages.exceptionToMessage(error)
                continuation.resume(
                    AwsAuthSignUpResult.Error(
                        exception = error,
                        message = errorMessage.messageId
                    )
                ) { _, _, _ -> }
            })
        }
    }

    override suspend fun confirmAccount(email: String, confirmCode: String): AwsAuthSignUpResult {
        return suspendCancellableCoroutine { continuation ->
            Amplify.Auth.confirmSignUp(email, confirmCode, { result ->
                continuation.resume(AwsAuthSignUpResult.IsSignedUp(result.isSignUpComplete)) { _, _, _ -> }
            }, { error ->
                val errorMessage = AuthExceptionMessages.exceptionToMessage(error)
                continuation.resume(
                    AwsAuthSignUpResult.Error(
                        exception = error,
                        message = errorMessage.messageId
                    )
                ) { _, _, _ -> }
            })
        }
    }

    override suspend fun resendConfirmationCode(email: String): AwsAuthSignUpResult {
        return suspendCancellableCoroutine { continuation ->
            Amplify.Auth.resendSignUpCode(email, { result ->
                continuation.resume(AwsAuthSignUpResult.ResentCode(result.destination)) { _, _, _ -> }
            }, { error ->
                val errorMessage = AuthExceptionMessages.exceptionToMessage(error)
                continuation.resume(
                    AwsAuthSignUpResult.Error(
                        exception = error,
                        message = errorMessage.messageId
                    )
                ) { _, _, _ -> }
            })
        }
    }

    override suspend fun getCurrentUserId(): Resource<String> {
        return suspendCancellableCoroutine { continuation ->
            Amplify.Auth.getCurrentUser({ onSuccess ->
                continuation.resume(Resource.Success(data = onSuccess.userId)) { _, _, _ -> }
            }, { error ->
                val errorMessage = AuthExceptionMessages.exceptionToMessage(error)
                continuation.resume(
                    Resource.Error(message = errorMessage.messageId)
                ) { _, _, _ -> }
            })
        }
    }

    override suspend fun getUserInfo(userId: String): Resource<UserInfo> {
        val userData = mapOf(
            "userID" to userId,
        )

        val jsonUserData = Gson().toJson(userData)
        val requestBody = jsonUserData.toByteArray()

        val request = RestOptions.builder().addPath("/users").addBody(requestBody).build()

        return suspendCancellableCoroutine { continuation ->
            Amplify.API.post(request, { response ->
                val dataAsJson = response.data.asJSONObject()
                val statusCode = dataAsJson.get("statusCode")
                val body = dataAsJson.getJSONObject("body")
                val userID = body.get("userID").toString()
                val userType = body.get("userType").toString()

                val userInfo = UserInfo(
                    userId = userID,
                    userType = userType,
                )

                continuation.resume(Resource.Success(userInfo)) { _, _, _ -> }
            }, { error ->
                continuation.resume(Resource.Error(error.message)) { _, _, _ -> }
            })
        }
    }

    override suspend fun updateUserType(userId: String, userType: String): Resource<UserInfo> {
        val userData = mapOf(
            "userID" to userId,
            "newUserType" to userType,
        )

        val jsonUserData = Gson().toJson(userData)
        val requestBody = jsonUserData.toByteArray()

        val request = RestOptions.builder().addPath("/UpdateUserType").addBody(requestBody).build()

        return suspendCancellableCoroutine { continuation ->
            Amplify.API.post(request, { response ->
                val dataAsJson = response.data.asJSONObject()
                val statusCode = dataAsJson.get("statusCode")
                val body = dataAsJson.getJSONObject("body")
                val updatedUser = body.getJSONObject("updatedUser")
                val userID = updatedUser.get("userID").toString()
                val newUserType = updatedUser.get("userType").toString()

                val userInfo = UserInfo(
                    userId = userID,
                    userType = newUserType,
                )

                continuation.resume(Resource.Success(userInfo)) { _, _, _ -> }
            }, { error ->
                continuation.resume(Resource.Error(error.message)) { _, _, _ -> }
            })
        }
    }

    override suspend fun fetchAuthSession(): Resource<String> {
        return suspendCancellableCoroutine { continuation ->
            Amplify.Auth.fetchAuthSession({ result ->
                val cognitoAuthSession = result as AWSCognitoAuthSession

                if (cognitoAuthSession.isSignedIn) {
                    val userId = cognitoAuthSession.userSubResult.value

                    if (!userId.isNullOrEmpty()) {
                        continuation.resume(Resource.Success(userId)) { _, _, _ -> }
                    } else {
                        continuation.resume(Resource.Error()) { _, _, _ -> }
                    }
                } else {
                    continuation.resume(Resource.Error()) { _, _, _ -> }
                }
            }, { error ->
                val errorMessage = AuthExceptionMessages.exceptionToMessage(error)
                continuation.resume(Resource.Error(message = errorMessage.messageId)) { _, _, _ -> }
            })
        }
    }

    override suspend fun signUpOrInWithGoogle(activity: Activity): Resource<String> {
        return suspendCancellableCoroutine { continuation ->
            Amplify.Auth.signInWithSocialWebUI(AuthProvider.google(), activity, {
                CoroutineScope(Dispatchers.IO).launch {
                    val result = fetchAuthSession()
                    if (result is Resource.Success && result.data != null) {
                        val userId = result.data

                        continuation.resume(Resource.Success(data = userId)) { _, _, _ -> }
                    } else {
                        continuation.resume(Resource.Error()) { _, _, _ -> }
                    }
                }
            }, { error ->
                val errorMessage = AuthExceptionMessages.exceptionToMessage(error)
                continuation.resume(Resource.Error(message = errorMessage.messageId)) { _, _, _ -> }
            })
        }
    }

    override suspend fun signOut() {
        Amplify.Auth.signOut {}
    }
}