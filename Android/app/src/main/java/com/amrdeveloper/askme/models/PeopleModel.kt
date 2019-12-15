package com.amrdeveloper.askme.models

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.amrdeveloper.askme.contracts.PeopleContract
import com.amrdeveloper.askme.events.LoadFinishEvent
import org.greenrobot.eventbus.EventBus

class PeopleModel : PeopleContract.Model{

    override fun loadPeopleFromServer(
        userViewModel: UserViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        userViewModel.getUserPagedList().observe(lifecycleOwner, Observer {
            EventBus.getDefault().post(LoadFinishEvent(it))
        })
    }
}