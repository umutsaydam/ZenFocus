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
    fun queryProductDetails()
    fun launchPurchaseFlow(productDetails: ProductDetails)
    fun handlePurchase(purchase: Purchase)
}