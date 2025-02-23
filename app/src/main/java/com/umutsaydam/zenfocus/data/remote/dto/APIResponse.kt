package com.umutsaydam.zenfocus.data.remote.dto


data class APIResponse(
    val statusCode: Int,
    val body : Items
)

data class Items(
    val items: List<ThemeInfo>
)

data class ThemeInfo(
    val themeName: String,
    val themeUrl: String,
    val themePrice: Int,
    val currency: String,
    val themeType: String,
    val videoThumbnailUrl: String?
)
