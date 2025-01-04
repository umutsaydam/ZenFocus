package com.umutsaydam.zenfocus.domain.usecases.remote

import com.umutsaydam.zenfocus.domain.repository.remote.GoogleProductsInAppRepository
import kotlinx.coroutines.flow.StateFlow

interface GoogleProductsInAppUseCases{
    fun startConnection()
    fun observePurchaseStateFlow(): StateFlow<Boolean>
}

class GoogleProductsInAppUseCasesImpl(
    private val googleProductsInAppRepository: GoogleProductsInAppRepository
): GoogleProductsInAppUseCases {

    override fun startConnection() {
        googleProductsInAppRepository.startConnection()
    }

    override fun observePurchaseStateFlow(): StateFlow<Boolean> {
        return googleProductsInAppRepository.purchaseStateFlow
    }
}