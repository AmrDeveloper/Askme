package com.amrdeveloper.askme

import androidx.lifecycle.LifecycleOwner
import com.amrdeveloper.askme.contracts.ProfileContract
import com.amrdeveloper.askme.models.FeedViewModel

class ProfilePresenter(
    val view: ProfileContract.View,
    val feedViewModel: FeedViewModel,
    val owner: LifecycleOwner
) : ProfileContract.Presenter {

    private val model : ProfileContract.Model

    init {
        model = ProfileModel()
    }

    override fun startLoadingFeed(userId: String) {
        view.showProgressBar()
        model.loadFeedFromServer(feedViewModel, owner)
    }
}