package com.amrdeveloper.askme.contracts

import androidx.lifecycle.LifecycleOwner
import com.amrdeveloper.askme.models.NotificationViewModel

interface NotificationContract {

    interface Model {
        fun loadFeedFromServer(notificationModel: NotificationViewModel, lifecycleOwner: LifecycleOwner)
    }

    interface View{
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter{
        fun startLoadingNotifications()
    }
}