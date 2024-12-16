package com.umutsaydam.zenfocus.data.local.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.umutsaydam.zenfocus.domain.repository.local.NetworkCheckerRepository
import dagger.hilt.android.qualifiers.ApplicationContext

class NetworkCheckerRepositoryImpl(
    @ApplicationContext private val context: Context
) : NetworkCheckerRepository {
    override fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}