package com.umutsaydam.zenfocus.data.remote.service

import android.util.Log
import com.amplifyframework.api.rest.RestOptions
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.step.AuthSignInStep
import com.amplifyframework.auth.result.step.AuthSignUpStep
import com.amplifyframework.core.Amplify
import com.google.gson.Gson
import com.umutsaydam.zenfocus.data.remote.dto.UserInfo
import com.umutsaydam.zenfocus.domain.service.AwsAuthService
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignInResult
import com.umutsaydam.zenfocus.domain.model.AwsAuthSignUpResult
import com.umutsaydam.zenfocus.domain.model.Resource
import kotlinx.coroutines.suspendCancellableCoroutine

class AwsAuthServiceImpl : AwsAuthService {

    override suspend fun signIn(email: String, password: String): AwsAuthSignInResult {
        return suspendCancellableCoroutine { continuation ->
            Amplify.Auth.signIn(
                email,
                password,
                { result ->
                    Log.e(
                        "R/T",
                        "$result"
                    )
                    when {
                        result.isSignedIn -> {
                            continuation.resume(AwsAuthSignInResult.IsSignedIn(result.isSignedIn)) { cause, _, _ ->
                                Log.e(
                                    "R/T",
                                    "Coroutine cancelled: $cause"
                                )
                            }
                        }

                        result.nextStep.signInStep == AuthSignInStep.CONFIRM_SIGN_UP -> {
                            continuation.resume(AwsAuthSignInResult.ConfirmSignIn(result.nextStep.signInStep)) { cause, _, _ ->
                                Log.e(
                                    "R/T",
                                    "Coroutine cancelled: $cause"
                                )
                            }
                        }

                        else -> {
                            continuation.resume(
                                AwsAuthSignInResult.Error(
                                    AuthException(
                                        message = "Unexpected sign-in step",
                                        recoverySuggestion = ""
                                    )
                                )
                            ) { cause, _, _ ->
                                Log.e("R/T", "Coroutine cancelled: $cause")
                            }
                        }
                    }
                },
                { error ->
                    Log.e("R/T", "Sign in failed", error)
                    continuation.resume(AwsAuthSignInResult.Error(error)) { cause, _, _ ->
                        Log.e("R/T", "Coroutine cancelled: $cause")
                    }
                }
            )
        }
    }

    override suspend fun signUp(email: String, password: String): AwsAuthSignUpResult {
        return suspendCancellableCoroutine { continuation ->
            val options = AuthSignUpOptions.builder()
                .userAttributes(
                    listOf(
                        AuthUserAttribute(AuthUserAttributeKey.email(), email)
                    )
                )
                .build()

            Amplify.Auth.signUp(
                email,
                password,
                options,
                { result ->
                    Log.i("R/T", "Sign up successful: $result")

                    when {
                        result.isSignUpComplete -> {
                            continuation.resume(AwsAuthSignUpResult.IsSignedUp(result.isSignUpComplete)) { _, _, _ ->
                                Log.i("R/T", "IsSignedUp complete ${result.isSignUpComplete}")
                            }
                        }

                        result.nextStep.signUpStep == AuthSignUpStep.CONFIRM_SIGN_UP_STEP -> {
                            continuation.resume(AwsAuthSignUpResult.ConfirmSignUp(AuthSignUpStep.CONFIRM_SIGN_UP_STEP)) { _, _, _ ->
                                Log.i(
                                    "R/T",
                                    "IsSignedUp has not completed yet. ${result.nextStep.signUpStep}"
                                )
                            }
                        }

                        else -> {
                            continuation.resume(
                                AwsAuthSignUpResult.Error(
                                    AuthException(
                                        message = "Unexpected sign-up step",
                                        recoverySuggestion = ""
                                    )
                                )
                            ) { cause, _, _ ->
                                Log.e("R/T", "Coroutine cancelled: $cause")
                            }
                        }
                    }
                },
                { onError ->
                    Log.e("R/T", "Sign up failed", onError)
                    continuation.resume(AwsAuthSignUpResult.Error(onError)) { cause, _, _ ->
                        Log.e("R/T", "Coroutine cancelled: $cause")
                    }
                }
            )
        }
    }

    override suspend fun confirmAccount(email: String, confirmCode: String): AwsAuthSignUpResult {
        return suspendCancellableCoroutine { continuation ->
            Amplify.Auth.confirmSignUp(
                email,
                confirmCode,
                { result ->
                    Log.d("R/T", "${result.isSignUpComplete}")
                    Log.d("R/T", "$result")

                    continuation.resume(AwsAuthSignUpResult.IsSignedUp(result.isSignUpComplete)) { cause, _, _ ->
                        Log.e("R/T", "Coroutine cancelled success: $cause")
                    }
                },
                { error ->
                    Log.i("R/T", "$error")
                    continuation.resume(AwsAuthSignUpResult.Error(error)) { cause, _, _ ->
                        Log.e("R/T", "Coroutine cancelled error : $cause")
                    }
                }
            )
        }
    }

    override suspend fun getCurrentUserId(): Resource<String> {
        return suspendCancellableCoroutine { continuation ->
            Amplify.Auth.getCurrentUser(
                { onSuccess ->
                    Log.i("R/T", "Result: ${onSuccess.userId}")
                    continuation.resume(Resource.Success(data = onSuccess.userId)) { cause, _, _ ->
                        Log.e("R/T", "Coroutine cancelled onSuccess: $cause")
                    }
                },
                { error ->
                    Log.i("R/T", "Error: $error")
                    continuation.resume(
                        Resource.Error(
                            message = error.message ?: "Unknown error"
                        )
                    ) { cause, _, _ ->
                        Log.e("R/T", "Coroutine cancelled error: $cause")
                    }
                }
            )
        }
    }

    override suspend fun getUserInfo(userId: String): Resource<UserInfo> {
        val userData = mapOf(
            "userID" to userId,
        )

        val jsonUserData = Gson().toJson(userData)
        val requestBody = jsonUserData.toByteArray()

        val request = RestOptions.builder()
            .addPath("/users")
            .addBody(requestBody)
            .build()

        return suspendCancellableCoroutine { continuation ->
            Amplify.API.post(request,
                { response ->
                    Log.i("R/T", "User added to DynamoDB: ${response.code}}")
                    Log.i("R/T", "User added to DynamoDB: $response")
                    Log.i("R/T", response.data.asJSONObject().toString())

                    val dataAsJson = response.data.asJSONObject()
                    val statusCode = dataAsJson.get("statusCode")
                    val body = dataAsJson.getJSONObject("body")
                    val userID = body.get("userID").toString()
                    val userType = body.get("userType").toString()

                    Log.i("R/T", ">>>> $userID}")
                    Log.i("R/T", ">>>> $userType")

                    val userInfo = UserInfo(
                        userId = userID,
                        userType = userType,
                    )

                    continuation.resume(Resource.Success(userInfo)) { cause, _, _ ->
                        Log.e("R/T", "Coroutine cancelled onSuccess: $cause")
                    }
                },
                { error ->
                    Log.e("R/T", "Failed to add user to DynamoDB", error)
                    continuation.resume(Resource.Error(error.message)) { cause, _, _ ->
                        Log.e("R/T", "Coroutine cancelled onSuccess: $cause")
                    }
                }
            )
        }
    }

    override suspend fun signOut() {
        Amplify.Auth.signOut {
            Log.i("AuthSignOut", "Signed out successfully")
        }
    }

    /*
        fun signUpOrInWithGoogle() {
            TODO("Not yet implemented")
        }
     */
}
