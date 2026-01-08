package com.umutsaydam.zenfocus.domain.repository.remote

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import kotlinx.coroutines.flow.StateFlow

interface GoogleProductsInAppRepository {
    val purchaseStateFlow: StateFlow<Boolean>

    fun setPurchasesUpdatedListener()
    fun buildBillingClient()
    fun startConnection(activity: Activity)
    fun queryPurchaseAsync()
    suspend fun queryProductDetails(): List<ProductDetails>
    fun launchPurchaseFlow(productDetails: ProductDetails, activity: Activity)
    fun handlePurchase(purchase: Purchase)
}