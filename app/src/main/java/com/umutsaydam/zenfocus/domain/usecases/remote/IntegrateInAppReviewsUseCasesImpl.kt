package com.umutsaydam.zenfocus.domain.usecases.remote

import android.content.Context
import com.google.android.play.core.review.ReviewInfo
import com.umutsaydam.zenfocus.domain.repository.remote.IntegrateInAppReviewsRepository

interface IntegrateInAppReviewsUseCases {
    fun startRequestReviewFlow(context: Context, onComplete: (ReviewInfo?) -> Unit)
    fun startReviewFlow(context: Context, reviewInfo: ReviewInfo, onSuccess: () -> Unit)
}

class IntegrateInAppReviewsUseCasesImpl(
    private val integrateInAppReviewsRepository: IntegrateInAppReviewsRepository
) : IntegrateInAppReviewsUseCases {
    override fun startRequestReviewFlow(context: Context, onComplete: (ReviewInfo?) -> Unit) {
        integrateInAppReviewsRepository.requestReviewFlow(context) { task ->
            onComplete(task)
        }
    }

    override fun startReviewFlow(context: Context, reviewInfo: ReviewInfo, onSuccess: () -> Unit) {
        integrateInAppReviewsRepository.launchReviewFlow(context, reviewInfo, onSuccess)
    }
}