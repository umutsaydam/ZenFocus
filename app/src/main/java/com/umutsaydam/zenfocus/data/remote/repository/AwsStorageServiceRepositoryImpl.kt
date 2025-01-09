package com.umutsaydam.zenfocus.data.remote.repository

import android.content.Context
import com.amplifyframework.api.rest.RestOptions
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StoragePath
import com.amplifyframework.storage.options.StorageDownloadFileOptions
import com.google.gson.Gson
import com.umutsaydam.zenfocus.data.remote.dto.APIResponse
import com.umutsaydam.zenfocus.domain.repository.remote.AwsStorageServiceRepository
import com.umutsaydam.zenfocus.domain.model.Resource
import com.umutsaydam.zenfocus.util.FileNameFromUrl
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File

class AwsStorageServiceRepositoryImpl(
    @ApplicationContext private val context: Context
) : AwsStorageServiceRepository {
    override suspend fun getThemeList(): Resource<APIResponse> {
        return suspendCancellableCoroutine { continuation ->
            val request = RestOptions.builder()
                .addPath("/images")
                .build()

            Amplify.API.get(
                request,
                { response ->
                    val responseData = response.data.asString()
                    val gson = Gson()
                    val apiResponse = gson.fromJson(responseData, APIResponse::class.java)

                    continuation.resume(Resource.Success(apiResponse)) { _, _, _ -> }
                },
                { failure ->
                    continuation.resume(Resource.Error(failure.message)) { _, _, _ -> }
                }
            )

        }
    }

    override suspend fun downloadSelectedTheme(selectedThemeUrl: String): Resource<String> {
        val selectedThemeName = FileNameFromUrl.getFileNameFromUrl(selectedThemeUrl)

        return suspendCancellableCoroutine { continuation ->
            Amplify.Storage.downloadFile(
                StoragePath.fromString(selectedThemeName),
                File("${context.filesDir}/$selectedThemeName"),
                StorageDownloadFileOptions.defaultInstance(),
                { result ->
                    continuation.resume(Resource.Success(data = result.file.name)) { _, _, _ -> }
                },
                { error ->
                    continuation.resume(Resource.Error(error.message)) { _, _, _ -> }
                }
            )
        }
    }
}