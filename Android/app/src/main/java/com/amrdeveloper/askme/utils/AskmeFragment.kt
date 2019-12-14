package com.amrdeveloper.askme.utils

import androidx.fragment.app.Fragment
import org.greenrobot.eventbus.EventBus

open class AskmeFragment : Fragment(){

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