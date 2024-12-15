package com.umutsaydam.zenfocus.data.local.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.umutsaydam.zenfocus.R
import com.umutsaydam.zenfocus.domain.repository.local.ThemeRepository
import com.umutsaydam.zenfocus.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject


class ThemeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ThemeRepository {
    override suspend fun getTheme(theme: String): Bitmap? {
        val file = File(context.filesDir, theme)
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }

    override fun copyDefaultThemeToInternalStorage() {
        val file = File(context.filesDir, Constants.DEFAULT_THEME)
        if (!file.exists()) {
            val inputStream = context.resources.openRawResource(R.raw.default_theme)
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            Log.i("FileCopy", "Dosya iç depolamaya kopyalandı: ${file.absolutePath}")
        } else {
            Log.i("FileCopy", "Dosya zaten var: ${file.absolutePath}")
        }
    }

    override fun isThemeAvailableInLocalStorage(themeName: String): Boolean {
        val file = File(context.filesDir, themeName)
        return file.exists()
    }
}