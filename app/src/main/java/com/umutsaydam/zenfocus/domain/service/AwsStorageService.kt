package com.umutsaydam.zenfocus.domain.service

import com.umutsaydam.zenfocus.data.remote.dto.APIResponse
import com.umutsaydam.zenfocus.util.Resource

interface AwsStorageService {
    suspend fun getThemeList(): Resource<APIResponse>

    suspend fun downloadSelectedTheme(selectedThemeUrl: String): Resource<String>
}