package com.umutsaydam.zenfocus.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.amazonaws.services.cognitoidentityprovider.model.InvalidParameterException
import com.amazonaws.services.cognitoidentityprovider.model.InvalidPasswordException
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    fun signUp(email: String, password: String) {
        Amplify.Auth.signUp(
            email,
            password,
            AuthSignUpOptions.builder().build(),
            { result ->
                Log.i("AuthQuickstart", "Result: $result")
                if (result.isSignUpComplete) {
                    val userID = Amplify.Auth.currentUser.userId
//                    val confirmCode = 124
//                    Amplify.Auth.confirmSignUp(email, confirmCode)
                }
            },
            { error ->
                Log.e("AuthQuickstart", "Sign up failed", error)
                when (error.cause) {
                    is InvalidParameterException -> {}
                    is InvalidPasswordException -> {}
                }
            }
        )
    }

    fun signIn(email: String, password: String) {
        Amplify.Auth.signIn(
            email,
            password,
            { result ->
                Log.i("AuthQuickstart", "Result: $result")
            },
            { error -> Log.e("AuthQuickstart", "Sign in failed", error) }
        )
    }

    fun signInWithGoogle() {
        //TODO perform sign in with google
    }
}