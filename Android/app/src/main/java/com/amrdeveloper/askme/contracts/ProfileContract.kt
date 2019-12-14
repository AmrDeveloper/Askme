package com.amrdeveloper.askme.contracts

import androidx.lifecycle.LifecycleOwner
import com.amrdeveloper.askme.models.FeedViewModel

interface ProfileContract {

    interface Model {
        fun loadFeedFromServer(feedModel: FeedViewModel, lifecycleOwner: LifecycleOwner)
    }

    interface View {
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter {
        fun startLoadingFeed()
    }
}