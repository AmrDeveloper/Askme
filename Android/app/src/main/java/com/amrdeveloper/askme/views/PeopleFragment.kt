package com.amrdeveloper.askme.views

import androidx.fragment.app.Fragment
import org.greenrobot.eventbus.EventBus

class PeopleFragment : Fragment() {


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