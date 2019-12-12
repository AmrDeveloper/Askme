package com.amrdeveloper.askme.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.amrdeveloper.askme.extensions.notNull

class NetworkReceiver(private val listener: NetworkStateListener) : BroadcastReceiver() {

    override fun onReceive(context : Context?, intent: Intent?) {
        context.notNull {
            if(isNetworkConnected(it)){
                listener.onNetworkConnected()
            }else{
                listener.onNetworkDisconnected()
            }
        }
    }
}