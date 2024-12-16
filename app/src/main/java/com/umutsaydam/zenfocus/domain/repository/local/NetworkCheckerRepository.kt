package com.umutsaydam.zenfocus.domain.repository.local

interface NetworkCheckerRepository {
    fun isConnected(): Boolean
}