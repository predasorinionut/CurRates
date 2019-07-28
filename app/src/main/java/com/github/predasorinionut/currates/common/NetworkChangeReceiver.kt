package com.github.predasorinionut.currates.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkChangeReceiver constructor(private val listener: OnNetworkChangeListener) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            if (isNetworkAvailable(context)) {
                listener.onNetworkAvailable()
            } else {
                listener.onNetworkUnavailable()
            }
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

        return activeNetwork?.isConnectedOrConnecting == true
    }

    interface OnNetworkChangeListener {
        fun onNetworkAvailable()
        fun onNetworkUnavailable()
    }
}