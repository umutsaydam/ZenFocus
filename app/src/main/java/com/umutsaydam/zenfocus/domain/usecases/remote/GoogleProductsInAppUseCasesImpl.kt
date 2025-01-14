package com.umutsaydam.zenfocus.domain.usecases.remote
// These lines are commented for the open source contribution.
//import android.app.Activity
//import com.umutsaydam.zenfocus.domain.repository.remote.GoogleProductsInAppRepository
//import kotlinx.coroutines.flow.StateFlow
//
//interface GoogleProductsInAppUseCases{
//    fun startConnection(activity: Activity)
//    fun observePurchaseStateFlow(): StateFlow<Boolean>
//}
//
//class GoogleProductsInAppUseCasesImpl(
//    private val googleProductsInAppRepository: GoogleProductsInAppRepository
//): GoogleProductsInAppUseCases {
//
//    override fun startConnection(activity: Activity) {
//        googleProductsInAppRepository.startConnection(activity)
//    }
//
//    override fun observePurchaseStateFlow(): StateFlow<Boolean> {
//        return googleProductsInAppRepository.purchaseStateFlow
//    }
//}