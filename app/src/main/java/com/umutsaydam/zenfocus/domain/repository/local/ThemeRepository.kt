package com.umutsaydam.zenfocus.domain.repository.local

import android.graphics.Bitmap

interface ThemeRepository {
    suspend fun getTheme(theme: String): Bitmap?

    fun copyDefaultThemeToInternalStorage()

    fun isThemeAvailableInLocalStorage(themeName: String): Boolean
}