package com.umutsaydam.zenfocus.data.remote.service

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.umutsaydam.zenfocus.BuildConfig
import com.umutsaydam.zenfocus.domain.service.GoogleAdService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred

class GoogleAdServiceImpl(
    @ApplicationContext private val context: Context
) : GoogleAdService {
    override suspend fun showRewardedAd(): RewardedAd? {
        val deferred = CompletableDeferred<RewardedAd?>()
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            context,
            BuildConfig.AD_REWARD_THEME_UNIT_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(p0: RewardedAd) {
                    super.onAdLoaded(p0)
                    Log.i("A/D", "Reward Ad loaded")
                    deferred.complete(p0)
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.i("A/D", "Reward Ad failed to load")
                    deferred.complete(null)
                }
            }
        )
        return deferred.await()
    }

    override fun showBannerAd(
        adSize: AdSize,
        onAdLoaded: (Boolean) -> Unit,
        onFirstAdRequested: (Boolean) -> Unit
    ): AdView {
        return AdView(context).apply {
            setAdSize(adSize)
            adUnitId = BuildConfig.AD_BANNER_UNIT_ID
            loadAd(AdRequest.Builder().build())

            adListener = object : AdListener() {
                override fun onAdClicked() {
                    Log.i("A/D", "onAdClicked")
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    Log.i("A/D", "onAdClosed")
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.i("A/D", "onAdFailedToLoad $p0")
                    onAdLoaded(false)
                    onFirstAdRequested(true)
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    Log.i("A/D", "onAdImpression")
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.i("A/D", "onAdLoaded")
                    onAdLoaded(true)
                    onFirstAdRequested(true)
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                    Log.i("A/D", "onAdOpened")
                }

                override fun onAdSwipeGestureClicked() {
                    super.onAdSwipeGestureClicked()
                    Log.i("A/D", "onAdSwipeGestureClicked")
                }
            }
        }
    }
}