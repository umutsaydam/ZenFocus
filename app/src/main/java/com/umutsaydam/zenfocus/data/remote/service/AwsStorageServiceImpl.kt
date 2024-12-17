package com.umutsaydam.zenfocus.data.remote.service

import android.content.Context
import android.util.Log
import com.amplifyframework.api.rest.RestOptions
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StoragePath
import com.amplifyframework.storage.options.StorageDownloadFileOptions
import com.google.gson.Gson
import com.umutsaydam.zenfocus.data.remote.dto.APIResponse
import com.umutsaydam.zenfocus.domain.service.AwsStorageService
import com.umutsaydam.zenfocus.util.FileNameFromUrl
import com.umutsaydam.zenfocus.domain.model.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import javax.inject.Inject

class AwsStorageServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AwsStorageService {
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
                    Log.i("R/T", "apiResponse: $apiResponse")

                    continuation.resume(Resource.Success(apiResponse)) { cause, _, _ ->
                        Log.i("R/T", "coroutine was canceled $cause")
                    }
                },
                { failure ->
                    Log.i("R/T", "Images fetched: $failure")
                    continuation.resume(Resource.Error(failure.message)) { cause, _, _ ->
                        Log.i("R/T", "coroutine was canceled $cause")
                    }
                }
            )

        }
    }

    override suspend fun downloadSelectedTheme(selectedThemeUrl: String): Resource<String> {
        val selectedThemeName = FileNameFromUrl.getFileNameFromUrl(selectedThemeUrl)
        Log.i("R/T", "selectedThemeName: $selectedThemeName")

       return suspendCancellableCoroutine { continuation ->
           Amplify.Storage.downloadFile(
               StoragePath.fromString(selectedThemeName),
               File("${context.filesDir}/$selectedThemeName"),
               StorageDownloadFileOptions.defaultInstance(),
               { progress ->
                   Log.i("R/T", progress.toString())
               },
               { result ->
                   Log.i("R/T", "File downloaded: ${result.file.path}")
                   Log.i("R/T", "File downloaded: ${result.file.name}")
                   continuation.resume(Resource.Success(data = result.file.name)){ cause, _, _ ->
                       Log.i("R/T", "coroutine was canceled $cause")
                   }
               },
               { error ->
                   Log.e("R/T", "Download failed", error)
               }
           )
       }
    }
}