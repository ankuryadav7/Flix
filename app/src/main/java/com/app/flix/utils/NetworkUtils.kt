package com.app.flix.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.app.flix.App

/**
 * Used for checking the internet connectivity
 * */
class NetworkUtils {
    companion object {
        private const val TYPE_WIFI = "wifi"
        private const val TYPE_ETHERNET = "ethernet"
        private const val TYPE_CELLULAR = "cellular"
        private const val TYPE_NONE = "none"

        /**
         * This network check is
         * applied from Android Q
         *
         * @return String
         */
        private val networkInfo: String
            get() {
                val connectivityManager = App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return TYPE_ETHERNET
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return TYPE_WIFI
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return TYPE_CELLULAR
                    }
                }
                return TYPE_NONE
            }
        val isNetworkAvailable: Boolean
            get() {
                return !networkInfo.equals(TYPE_NONE, ignoreCase = true)
            }
        val isConnectedWifi: Boolean
            get() {
                return networkInfo.equals(TYPE_WIFI, ignoreCase = true)
            }
        val isConnectedEthernet: Boolean
            get() {
                return networkInfo.equals(TYPE_ETHERNET, ignoreCase = true)
            }
    }

    init {
        throw IllegalAccessError("Utility class")
    }
}