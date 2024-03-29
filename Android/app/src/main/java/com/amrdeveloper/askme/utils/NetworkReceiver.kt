package com.amrdeveloper.askme.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NetworkReceiver(private val listener: NetworkStateListener) : BroadcastReceiver() {

    override fun onReceive(context : Context, intent: Intent?) {
        if (isNetworkConnected(context)){
            listener.onNetworkConnected()
        }else{
            listener.onNetworkDisconnected()
        }
    }
}