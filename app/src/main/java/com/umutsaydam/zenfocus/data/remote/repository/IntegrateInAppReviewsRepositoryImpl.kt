package com.umutsaydam.zenfocus.data.remote.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.umutsaydam.zenfocus.domain.repository.remote.IntegrateInAppReviewsRepository

class IntegrateInAppReviewsRepositoryImpl : IntegrateInAppReviewsRepository {
    private lateinit var reviewManager: ReviewManager

    override fun requestReviewFlow(context: Context, completeCallback: (ReviewInfo?) -> Unit) {
        Log.i("R/T", "requestReviewFlow is starting...")
        reviewManager = ReviewManagerFactory.create(context)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i("R/T", "requestReviewFlow is successful...")
                val reviewInfo = task.result
                completeCallback(reviewInfo)
            } else {
                Log.i("R/T", "requestReviewFlow is  null...")
                completeCallback(null)
            }
        }
    }

    override fun launchReviewFlow(context: Context, reviewInfo: ReviewInfo, onSuccess: () -> Unit) {
        val activity = context as Activity
        val flow = reviewManager.launchReviewFlow(activity, reviewInfo)
        flow.addOnCompleteListener { a ->
            if (a.isSuccessful) {
                onSuccess()
            }
            // The flow has finished. The API does not indicate whether the user
            // reviewed or not, or even whether the review dialog was shown. Thus, no
            // matter the result, we continue our app flow.
        }
    }
}