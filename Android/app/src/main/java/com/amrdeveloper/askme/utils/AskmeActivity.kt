package com.amrdeveloper.askme.utils

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.amrdeveloper.askme.utils.NetworkReceiver
import com.amrdeveloper.askme.utils.NetworkStateListener
import org.greenrobot.eventbus.EventBus

abstract class AskmeActivity : AppCompatActivity() {

    abstract fun onNetworkOn()
    abstract fun onNetworkOff()
    abstract fun onThemeChanged()

    private lateinit var mNetworkReceiver : NetworkReceiver

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        mNetworkReceiver = NetworkReceiver(mNetworkChangeListener)
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

    private val mNetworkChangeListener : NetworkStateListener = object : NetworkStateListener{
        override fun onNetworkConnected() {
            onNetworkOn()
        }

        override fun onNetworkDisconnected() {
            onNetworkOff()
        }
    }
}