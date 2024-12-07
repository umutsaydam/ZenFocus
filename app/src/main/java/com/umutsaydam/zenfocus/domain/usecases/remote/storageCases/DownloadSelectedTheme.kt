package com.umutsaydam.zenfocus.domain.usecases.remote.storageCases

import com.umutsaydam.zenfocus.domain.repository.remote.AwsStorageServiceRepository
import com.umutsaydam.zenfocus.util.Resource

class DownloadSelectedTheme(
    private val awsStorageServiceRepository: AwsStorageServiceRepository
) {
    suspend operator fun invoke(selectedThemeUrl: String): Resource<String> {
        return awsStorageServiceRepository.downloadSelectedTheme(selectedThemeUrl)
    }
}