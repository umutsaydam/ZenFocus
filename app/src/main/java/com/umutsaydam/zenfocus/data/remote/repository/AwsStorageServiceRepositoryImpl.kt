package com.umutsaydam.zenfocus.data.remote.repository

import com.umutsaydam.zenfocus.data.remote.dto.APIResponse
import com.umutsaydam.zenfocus.domain.repository.remote.AwsStorageServiceRepository
import com.umutsaydam.zenfocus.domain.service.AwsStorageService
import com.umutsaydam.zenfocus.domain.model.Resource

class AwsStorageServiceRepositoryImpl(
    private val awsStorageService: AwsStorageService
) : AwsStorageServiceRepository {
    override suspend fun getThemeList(): Resource<APIResponse> {
        return awsStorageService.getThemeList()
    }

    override suspend fun downloadSelectedTheme(selectedThemeUrl: String): Resource<String> {
        return awsStorageService.downloadSelectedTheme(selectedThemeUrl)
    }
}