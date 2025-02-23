package com.umutsaydam.zenfocus.domain.usecases.local

import android.graphics.Bitmap
import com.umutsaydam.zenfocus.domain.repository.local.ThemeRepository

interface ThemeRepositoryUseCases{
    suspend fun getTheme(theme: String): Bitmap?
    fun copyDefaultThemeToInternalStorage()
    fun isThemeAvailableInLocalStorage(themeName: String): Boolean
}

class ThemeRepositoryUseCasesImpl(
    private val themeRepository: ThemeRepository
): ThemeRepositoryUseCases {
    override suspend fun getTheme(theme: String): Bitmap? {
        return themeRepository.getTheme(theme)
    }

    override fun copyDefaultThemeToInternalStorage() {
        themeRepository.copyDefaultThemeToInternalStorage()
    }

    override fun isThemeAvailableInLocalStorage(themeName: String): Boolean {
        return themeRepository.isThemeAvailableInLocalStorage(themeName)
    }
}