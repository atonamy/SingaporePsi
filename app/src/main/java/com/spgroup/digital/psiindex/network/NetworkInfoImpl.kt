package com.spgroup.digital.psiindex.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat
import com.spgroup.digital.psiindex.R

class NetworkInfoImpl(private val context: Context) : NetworkInfo {

    inner class NetworkChangeReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            listener?.invoke()
        }

    }

    private val receiver = NetworkChangeReceiver()
    private var listener: (() -> Unit)? = null

    override val whenOfflineMessage: String = context.getString(R.string.offline_error)

    override fun unregisterNetworkStateListener() {
        if(listener != null)
            context.unregisterReceiver(receiver)
        listener = null
    }

    override fun registerNetworkStateListener(listener: () -> Unit) {
        unregisterNetworkStateListener()
        context.registerReceiver(receiver, IntentFilter().apply {
            addAction("android.net.conn.CONNECTIVITY_CHANGE")
        })
        this.listener = listener
    }

    override val isNetworkAvailable: Boolean
        get() {
            val manager = ContextCompat.getSystemService(context, ConnectivityManager::class.java)
            val networkInfo = manager?.activeNetworkInfo
            return (networkInfo != null && networkInfo.isConnected)
        }
}