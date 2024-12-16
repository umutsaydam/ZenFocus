package com.umutsaydam.zenfocus.domain.usecases.local

import com.umutsaydam.zenfocus.domain.repository.local.NetworkCheckerRepository

interface NetworkCheckerUseCases {
    fun isConnected(): Boolean
}

class NetworkCheckerUseCasesImpl(
    private val networkCheckerRepository: NetworkCheckerRepository
) : NetworkCheckerUseCases {
    override fun isConnected(): Boolean {
        return networkCheckerRepository.isConnected()
    }
}