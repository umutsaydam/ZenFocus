package com.umutsaydam.zenfocus.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.review.ReviewInfo
import com.umutsaydam.zenfocus.domain.usecases.local.LocalUserDataStoreCases
import com.umutsaydam.zenfocus.domain.usecases.remote.IntegrateInAppReviewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntegrateInAppReviewViewModel @Inject constructor(
    private val localUserDataStoreCases: LocalUserDataStoreCases,
    private val integrateInAppReviewsUseCases: IntegrateInAppReviewsUseCases
) : ViewModel() {
    private val _isAvailableForReview = MutableStateFlow(false)
    var isAvailableForReview: StateFlow<Boolean> = _isAvailableForReview

    private val _reviewInfo = MutableStateFlow<ReviewInfo?>(null)
    var reviewInfo: StateFlow<ReviewInfo?> = _reviewInfo

    init {
        getIsAvailableForReview()
    }

    private fun getIsAvailableForReview() {
        viewModelScope.launch {
            localUserDataStoreCases.readAvailableForReview().collectLatest { isAvailable ->
                _isAvailableForReview.value = isAvailable
            }
        }
    }

    private fun setIsNotAvailableForReview() {
        viewModelScope.launch {
            localUserDataStoreCases.saveAvailableForReview(false)
        }
    }

    fun launchReview(context: Context){
        integrateInAppReviewsUseCases.startRequestReviewFlow(context) { reviewInfo: ReviewInfo? ->
            _reviewInfo.value = reviewInfo
            if(reviewInfo != null){
                integrateInAppReviewsUseCases.startReviewFlow(context, reviewInfo){
                    setIsNotAvailableForReview()
                }
            }
        }
    }
}