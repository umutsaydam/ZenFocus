package com.umutsaydam.zenfocus.presentation.viewmodels

import android.app.Activity
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.domain.model.Resource
import com.umutsaydam.zenfocus.domain.model.UserTypeEnum
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.remote.AwsAuthCases
import com.umutsaydam.zenfocus.domain.usecases.remote.GoogleProductsInAppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaywallViewModel @Inject constructor(
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val googleProductsInAppUseCases: GoogleProductsInAppUseCases,
    private val authCases: AwsAuthCases
) : ViewModel() {
    private val _plans = MutableStateFlow<List<ProductDetails>>(emptyList())
    val plans: StateFlow<List<ProductDetails>> = _plans

    private var selectedPlan: ProductDetails? = null

    private val _sharedMessage = MutableSharedFlow<Int>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val sharedMessage = _sharedMessage.asSharedFlow()

    private val _shouldPopStack = MutableSharedFlow<Unit>()
    val shouldPopStack = _shouldPopStack.asSharedFlow()

    private var savedUserId = ""
    private var savedUserType = ""

    init {
        getUserId()
        getUserType()
        getPlans()
        observePremiumState()
        observePurchaseState()
    }

    private fun observePremiumState() {
        viewModelScope.launch {
            googleProductsInAppUseCases.getPurchaseFlow().collectLatest { isPremium ->
                if (isPremium) {
                    showMessage(R.string.became_ad_free_user)
                    _shouldPopStack.emit(Unit)
                }
            }
        }
    }

    private fun getPlans() {
        viewModelScope.launch {
            val plans = googleProductsInAppUseCases.getPlans()
            selectedPlan = plans.firstOrNull()
            _plans.value = plans
        }
    }

    fun buySelectedPlan(activity: Activity) {
        selectedPlan?.let {
            googleProductsInAppUseCases.buySelectedPlan(it, activity)
        }
    }

    private fun observePurchaseState() {
        viewModelScope.launch {
            googleProductsInAppUseCases.observePurchaseStateFlow().collect { purchaseState ->
                if (purchaseState) {
                    changeUserTypeAsAdFree()
                }
            }
        }
    }

    private fun getUserId() {
        viewModelScope.launch {
            savedUserId = localUserDataStoreCases.readUserId().first()
        }
    }

    private fun getUserType() {
        viewModelScope.launch {
            savedUserType = localUserDataStoreCases.readUserType().first()
        }
    }

    private fun changeUserTypeAsAdFree() {
        viewModelScope.launch {
            val adFreeUser = UserTypeEnum.AD_FREE_USER.type
            if (savedUserId.isNotEmpty() && savedUserType != adFreeUser) {
                val result = authCases.updateUserInfo(
                    userId = savedUserId, userType = adFreeUser
                )

                when (result) {
                    is Resource.Success -> {
                        showMessage(R.string.became_ad_free_user)
                    }

                    is Resource.Error -> {
                        showMessage(R.string.error_occurred_ad_free)
                    }
                }
            }
        }
    }

    fun restorePreviousPurchaseIfAvailable(){
        googleProductsInAppUseCases.queryPurchaseAsync()
    }

    private fun showMessage(@StringRes resId: Int) {
        viewModelScope.launch {
            _sharedMessage.emit(resId)
        }
    }
}