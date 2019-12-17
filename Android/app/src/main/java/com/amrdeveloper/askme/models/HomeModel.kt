package com.amrdeveloper.askme.models

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.amrdeveloper.askme.contracts.HomeContract
import com.amrdeveloper.askme.events.LoadFinishEvent
import org.greenrobot.eventbus.EventBus

class HomeModel : HomeContract.Model{

    override fun loadHomeFeedFromServer(viewModel: HomeViewModel, lifecycleOwner: LifecycleOwner) {
        viewModel.getFeedPagedList().observe(lifecycleOwner, Observer {
            EventBus.getDefault().post(LoadFinishEvent(it))
        })
    }
}