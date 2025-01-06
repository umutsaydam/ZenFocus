package com.umutsaydam.zenfocus.domain.usecases.remote

import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.rewarded.RewardedAd
import com.umutsaydam.zenfocus.domain.service.GoogleAdService

interface GoogleAdUseCases {
    suspend fun showRewardedAd(): RewardedAd?
    fun showBannerAd(
        adSize: AdSize,
        onAdLoaded: (Boolean) -> Unit,
        onFirstAdRequested: (Boolean) -> Unit
    ): AdView
}

class GoogleAdUseCasesImpl(
    private val googleAdService: GoogleAdService
) : GoogleAdUseCases {
    override suspend fun showRewardedAd(): RewardedAd? {
        return googleAdService.showRewardedAd()
    }

    override fun showBannerAd(
        adSize: AdSize,
        onAdLoaded: (Boolean) -> Unit,
        onFirstAdRequested: (Boolean) -> Unit
    ): AdView {
        return googleAdService.showBannerAd(
            adSize = adSize,
            onAdLoaded = onAdLoaded,
            onFirstAdRequested = onFirstAdRequested
        )
    }
}