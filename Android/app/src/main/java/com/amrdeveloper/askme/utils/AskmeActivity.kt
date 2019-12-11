package com.amrdeveloper.askme.utils

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import org.greenrobot.eventbus.EventBus

abstract class AskmeActivity : AppCompatActivity() {

    abstract fun onNetworkOn()
    abstract fun onNetworkOff()

    private var isNetworkConnected : Boolean = false
    private val mNetworkReceiver : NetworkReceiver

    init {
        mNetworkReceiver = NetworkReceiver( object : NetworkStateListener{
            override fun onNetworkConnected() {
                onNetworkOn()
                isNetworkConnected = true
            }

            override fun onNetworkDisconnected() {
                onNetworkOff()
                isNetworkConnected = false
            }
        })
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(mNetworkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mNetworkReceiver)
        EventBus.getDefault().unregister(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    fun isNetworkConnected() : Boolean = isNetworkConnected
}