package com.amrdeveloper.askme.presenters

import androidx.lifecycle.LifecycleOwner
import com.amrdeveloper.askme.contracts.ProfileContract
import com.amrdeveloper.askme.models.FeedViewModel
import com.amrdeveloper.askme.models.ProfileModel

class ProfilePresenter(
    private val view: ProfileContract.View,
    private val feedViewModel: FeedViewModel,
    private val owner: LifecycleOwner
) : ProfileContract.Presenter {

    private val model : ProfileContract.Model

    init {
        model = ProfileModel()
    }

    override fun startLoadingFeed() {
        view.showProgressBar()
        model.loadFeedFromServer(feedViewModel, owner)
    }
}