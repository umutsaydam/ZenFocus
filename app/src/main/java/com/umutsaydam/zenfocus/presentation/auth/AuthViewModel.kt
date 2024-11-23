package com.umutsaydam.zenfocus.presentation.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(): ViewModel() {

    fun signUp(email: String, password: String){
        //TODO check email and password are not empty or null
    }

    fun signIn(email: String, password: String){
        //TODO check email and password are not empty or null
    }

    fun signInWithGoogle(){
        //TODO perform sign in with google
    }
}