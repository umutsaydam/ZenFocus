package com.umutsaydam.zenfocus.domain.service

import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.rewarded.RewardedAd

interface GoogleAdService {
    suspend fun showRewardedAd(): RewardedAd?
    fun showBannerAd(
        adSize: AdSize,
        onAdLoaded: (Boolean) -> Unit,
        onFirstAdRequested: (Boolean) -> Unit
    ): AdView
}