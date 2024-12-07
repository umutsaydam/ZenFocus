package com.umutsaydam.zenfocus.domain.usecases.remote

import com.umutsaydam.zenfocus.domain.usecases.remote.storageCases.DownloadSelectedTheme
import com.umutsaydam.zenfocus.domain.usecases.remote.storageCases.ReadThemeList

data class AwsStorageCases(
    val readThemeList: ReadThemeList,
    val downloadSelectedThemeList: DownloadSelectedTheme
)
