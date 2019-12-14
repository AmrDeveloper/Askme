package com.amrdeveloper.askme.presenters

import androidx.lifecycle.LifecycleOwner
import com.amrdeveloper.askme.models.NotificationModel
import com.amrdeveloper.askme.models.NotificationViewModel
import com.amrdeveloper.askme.contracts.NotificationContract

class NotificationPresenter(
    private val view: NotificationContract.View,
    private val notiViewModel: NotificationViewModel,
    private val owner: LifecycleOwner
) : NotificationContract.Presenter {


    private val model: NotificationContract.Model

    init {
        model = NotificationModel()
    }

    override fun startLoadingNotifications() {
        view.showProgressBar()
        model.loadFeedFromServer(notiViewModel, owner)
    }
}