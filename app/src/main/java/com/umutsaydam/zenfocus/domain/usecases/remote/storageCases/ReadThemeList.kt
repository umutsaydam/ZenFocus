package com.umutsaydam.zenfocus.domain.usecases.remote.storageCases

import com.umutsaydam.zenfocus.data.remote.dto.APIResponse
import com.umutsaydam.zenfocus.domain.repository.remote.AwsStorageServiceRepository
import com.umutsaydam.zenfocus.util.Resource

class ReadThemeList(
    private val awsStorageServiceRepository: AwsStorageServiceRepository
) {
    suspend operator fun invoke(): Resource<APIResponse> {
       return awsStorageServiceRepository.getThemeList()
    }
}