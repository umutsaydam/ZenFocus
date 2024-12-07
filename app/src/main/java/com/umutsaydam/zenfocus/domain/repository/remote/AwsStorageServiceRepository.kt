package com.umutsaydam.zenfocus.domain.repository.remote

import com.umutsaydam.zenfocus.data.remote.dto.APIResponse
import com.umutsaydam.zenfocus.util.Resource

interface AwsStorageServiceRepository {
    suspend fun getThemeList(): Resource<APIResponse>
    suspend fun downloadSelectedTheme(selectedThemeUrl: String): Resource<String>
}