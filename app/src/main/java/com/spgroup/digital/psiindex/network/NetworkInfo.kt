package com.spgroup.digital.psiindex.network

import java.io.Closeable

interface NetworkInfo {
    val whenOfflineMessage: String
    val isNetworkAvailable: Boolean
    fun registerNetworkStateListener(listener: () -> Unit)
    fun unregisterNetworkStateListener()
}