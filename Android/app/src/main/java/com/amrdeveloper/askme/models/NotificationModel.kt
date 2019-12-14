package com.amrdeveloper.askme.models

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.amrdeveloper.askme.contracts.NotificationContract
import com.amrdeveloper.askme.events.LoadFinishEvent
import org.greenrobot.eventbus.EventBus

class NotificationModel : NotificationContract.Model {
    override fun loadFeedFromServer(
        notificationModel: NotificationViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        notificationModel.getNotiPagedList().observe(lifecycleOwner, Observer {
            EventBus.getDefault().post(LoadFinishEvent(it))
        })
    }
}