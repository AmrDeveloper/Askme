package com.amrdeveloper.askme.contracts

import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList
import com.amrdeveloper.askme.data.Feed
import com.amrdeveloper.askme.models.FeedViewModel

interface ProfileContract {

    interface Model {
        fun loadFeedFromServer(feedModel: FeedViewModel, lifecycleOwner: LifecycleOwner)
    }

    interface View {
        fun onLoadFinish(feedList: PagedList<Feed>)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter {
        fun startLoadingFeed(userId: String)
    }
}