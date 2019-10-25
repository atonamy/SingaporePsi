package test.mocks

import com.spgroup.digital.psiindex.network.NetworkInfo

class NetworkInfoMockImpl(
    private val isOnline: () -> Boolean,
    private val networkStateListener: (listener: () -> Unit) -> Unit
) : NetworkInfo {


    override val whenOfflineMessage: String
        get() = "offline_error_message"

    override val isNetworkAvailable: Boolean
        get() = isOnline()

    override fun registerNetworkStateListener(trigger: () -> Unit) =
        networkStateListener(trigger)

    override fun unregisterNetworkStateListener() {

    }
}