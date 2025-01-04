package com.umutsaydam.zenfocus.data.remote.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.umutsaydam.zenfocus.domain.repository.remote.GoogleProductsInAppRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GoogleProductsInAppRepositoryImpl(
    @ApplicationContext private val context: Context
) : GoogleProductsInAppRepository {
    private lateinit var purchasesUpdatedListener: PurchasesUpdatedListener
    private lateinit var billingClient: BillingClient

    private val _purchaseStateFlow = MutableStateFlow(false)
    override val purchaseStateFlow: StateFlow<Boolean> = _purchaseStateFlow

    init {
        setPurchasesUpdatedListener()
        buildBillingClient()
    }

    override fun setPurchasesUpdatedListener() {
        purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingResponseCode.OK && purchases != null) {
                Log.i("R/AD", "BillingResponseCode.OK")
                for (purchase in purchases) {
                    handlePurchase(purchase)
                }
            } else {
                Log.i("R/AD", "something went wrong... line 32")
            }
        }
    }

    override fun buildBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
            )
            .build()
    }

    override fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                Log.i("R/AD", "Billing Service Disconnected")
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingResponseCode.OK) {
                    Log.i("R/AD", "onBillingSetupFinished")
                    queryProductDetails()
                } else {
                    Log.i(
                        "R/AD",
                        "Billing Setup Failed: ${billingResult.debugMessage}"
                    )
                }
            }
        })
    }

    override fun queryProductDetails() {
        Log.i("R/AD", "starting queryProductDetails func...")
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("remove_ads")
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            Log.i(
                "R/AD",
                "starting queryProductDetails func billingResult ${billingResult.responseCode} ?= ${BillingResponseCode.OK}"
            )
            Log.i("R/AD", "productDetailsList: $productDetailsList")
            if (billingResult.responseCode == BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                val productDetails = productDetailsList.first()

                val activity = context as? Activity
                activity?.let {
                    Log.i("R/AD", "calling launchPurchaseFlow func...")
                    launchPurchaseFlow(
                        productDetails
                    )
                }
            }
        }
    }

    override fun launchPurchaseFlow(productDetails: ProductDetails) {
        Log.i("R/AD", "starting launchPurchaseFlow func...")
        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .build()

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()

        val activity = context as? Activity
        activity?.let {
            billingClient.launchBillingFlow(it, billingFlowParams)
        }
    }

    override fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingResponseCode.OK) {
                        Log.i("R/AD", "enable process to remove ads...")
                        _purchaseStateFlow.value = true
                    }
                }
            }
        }
    }

}