package com.amrdeveloper.askme

import androidx.appcompat.app.AppCompatActivity
import org.greenrobot.eventbus.EventBus

abstract class AskmeActivity : AppCompatActivity() {

    abstract fun onNetworkOn()
    abstract fun onNetworkOff()
    abstract fun onThemeChanged()

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}