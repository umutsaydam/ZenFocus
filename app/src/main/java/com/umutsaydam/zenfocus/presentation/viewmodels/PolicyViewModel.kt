package com.umutsaydam.zenfocus.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.umutsaydam.zenfocus.domain.usecases.local.NetworkCheckerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PolicyViewModel @Inject constructor(
    private val networkCheckerUseCases: NetworkCheckerUseCases
): ViewModel() {
    fun isNetworkConnected(): Boolean{
        return networkCheckerUseCases.isConnected()
    }
}