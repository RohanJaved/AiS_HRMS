package com.appinsnap.aishrm.util.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.appinsnap.aishrm.util.Constants

/**
 * Created by Adnan Bashir manak on 26,June,2023
 * AIS company,
 * Krachi, Pakistan.
 */
class NetworkChangeReceiver : BroadcastReceiver() {

    interface NetworkChangeListener {
        fun onNetworkChanged(isConnected: Boolean)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        val isConnected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false

        // Notify the listener about the network change
        if (context is NetworkChangeListener) {
            if (Constants.IS_MAIN_OPEN)
            {
                context.onNetworkChanged(isConnected)
            }
        }
    }


}