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
import com.android.billingclient.api.QueryPurchasesParams
import com.umutsaydam.zenfocus.domain.model.UserTypeEnum
import com.umutsaydam.zenfocus.domain.repository.remote.GoogleProductsInAppRepository
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsAuthCases
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class GoogleProductsInAppRepositoryImpl(
    @ApplicationContext private val context: Context,
    private val authCases: AwsAuthCases,
    private val localUserDataStoreCases: LocalUserDataStoreCases
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
                for (purchase in purchases) {
                    handlePurchase(purchase)
                }
            }
        }
    }

    override fun buildBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enableAutoServiceReconnection()
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
            )
            .build()
    }

    override fun startConnection(activity: Activity) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
//                Log.i("R/AD", "Billing Service Disconnected")
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingResponseCode.OK) {
                    CoroutineScope(Dispatchers.IO).launch {
                        queryProductDetails()
                    }
                    queryPurchaseAsync()
                }
//                else {
//                    Log.i(
//                        "R/AD",
//                        "Billing Setup Failed: ${billingResult.debugMessage}"
//                    )
//                }
            }
        })
    }

    override fun queryPurchaseAsync() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { _, purchases ->
            Log.d("R/T", "queryPurchaseAsync()")

            val isPremiumFromPlay = purchases.any {
                it.purchaseState == Purchase.PurchaseState.PURCHASED
            }
            Log.d("R/T", "isPremiumFromPlay $isPremiumFromPlay")

            _purchaseStateFlow.value = isPremiumFromPlay

            CoroutineScope(Dispatchers.IO).launch {
                onRestoreResult(isPremiumFromPlay)
            }
        }
    }

    override suspend fun queryProductDetails(): List<ProductDetails> {
        return suspendCancellableCoroutine { continuation ->
            val productList = listOf(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId("remove_ads")
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            )

            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build()

            billingClient.queryProductDetailsAsync(params) { billingResult, queryProductDetailsResult ->
                if (billingResult.responseCode == BillingResponseCode.OK) {
                    val productDetailsList = queryProductDetailsResult.productDetailsList
                    if (productDetailsList.isNotEmpty()) {
                        continuation.resume(productDetailsList.toList())
                    } else {
                        continuation.resume(emptyList())
                    }
                } else {
                    Log.d("R/T", "error: ${billingResult.debugMessage}")
                    continuation.resume(emptyList())
                }
            }
        }
    }

    override fun launchPurchaseFlow(productDetails: ProductDetails, activity: Activity) {
        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .build()
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()
        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    override fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingResponseCode.OK) {
                        _purchaseStateFlow.value = true
                    }
                }
            } else {
                _purchaseStateFlow.value = true
            }
        }
    }

    private suspend fun onRestoreResult(isPremiumFromPlay: Boolean) {
        val localPremium = localUserDataStoreCases.readUserType().first()

        val adFreeType = UserTypeEnum.AD_FREE_USER.type
        val normalType = UserTypeEnum.NORMAL_USER.type
        if ((localPremium == normalType && isPremiumFromPlay) || (localPremium == adFreeType && !isPremiumFromPlay)) {
            saveUserType(isPremiumFromPlay)
        }
    }

    private fun saveUserType(isPremium: Boolean) {
        Log.d("R/T", "saveUserType isPremium: $isPremium")
        CoroutineScope(Dispatchers.IO).launch {
            val currentUserType = if (isPremium) {
                UserTypeEnum.AD_FREE_USER
            } else {
                UserTypeEnum.NORMAL_USER
            }

            val userId = localUserDataStoreCases.readUserId().first()

            authCases.updateUserInfo(
                userId = userId,
                userType = currentUserType.type
            )
            Log.d("R/T", "localUserDataStoreCases.saveUserType: ${currentUserType.type}")
            localUserDataStoreCases.saveUserType(currentUserType.type)
        }
    }
}