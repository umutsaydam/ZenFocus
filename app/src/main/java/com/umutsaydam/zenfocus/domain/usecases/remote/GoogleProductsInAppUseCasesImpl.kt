package com.umutsaydam.zenfocus.domain.usecases.remote

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.umutsaydam.zenfocus.domain.repository.remote.GoogleProductsInAppRepository
import kotlinx.coroutines.flow.StateFlow

interface GoogleProductsInAppUseCases{
    fun getPurchaseFlow(): StateFlow<Boolean>
    fun startConnection(activity: Activity)
    fun queryPurchaseAsync()
    fun observePurchaseStateFlow(): StateFlow<Boolean>
    suspend fun getPlans(): List<ProductDetails>
    fun buySelectedPlan(productDetails: ProductDetails, activity: Activity)
}

class GoogleProductsInAppUseCasesImpl(
    private val googleProductsInAppRepository: GoogleProductsInAppRepository
): GoogleProductsInAppUseCases {

    override fun getPurchaseFlow(): StateFlow<Boolean> {
        return googleProductsInAppRepository.purchaseStateFlow
    }

    override fun startConnection(activity: Activity) {
        googleProductsInAppRepository.startConnection(activity)
    }

    override fun queryPurchaseAsync() {
        googleProductsInAppRepository.queryPurchaseAsync()
    }

    override fun observePurchaseStateFlow(): StateFlow<Boolean> {
        return googleProductsInAppRepository.purchaseStateFlow
    }

    override suspend fun getPlans(): List<ProductDetails> {
        return googleProductsInAppRepository.queryProductDetails()
    }

    override fun buySelectedPlan(productDetails: ProductDetails, activity: Activity) {
        return googleProductsInAppRepository.launchPurchaseFlow(productDetails, activity)
    }
}