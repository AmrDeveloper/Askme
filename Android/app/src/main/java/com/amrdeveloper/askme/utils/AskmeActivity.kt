package com.amrdeveloper.askme.utils

import androidx.appcompat.app.AppCompatActivity

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

    fun isNetworkConnected() : Boolean = isNetworkConnected
}