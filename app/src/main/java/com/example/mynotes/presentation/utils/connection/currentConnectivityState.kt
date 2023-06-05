package com.example.mynotes.presentation.utils.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

val Context.networkState: NetworkState
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityState(connectivityManager)
    }

private fun getCurrentConnectivityState(
    connectivityManager: ConnectivityManager
): NetworkState {
    val connected = connectivityManager.allNetworks.any { network ->
        connectivityManager.getNetworkCapabilities(network)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }

    return if (connected) NetworkState.AVAILABLE else NetworkState.UNAVAILABLE
}

sealed class NetworkState {
    object AVAILABLE : NetworkState()
    object UNAVAILABLE : NetworkState()
}