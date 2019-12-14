package com.amrdeveloper.askme.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.data.Feed

class FeedViewModel : ViewModel() {

    companion object{

        private lateinit var userId: String

        fun setUserId(id: String) {
            this.userId = id
        }
    }

    private var feedPagedList: LiveData<PagedList<Feed>>
    private var liveDataSource: LiveData<PageKeyedDataSource<Int, Feed>>

    init {
        val feedDataSourceFactory = FeedDataSourceFactory(userId)
        liveDataSource = feedDataSourceFactory.getFeedLiveDataSource()

        val config: PagedList.Config =
            PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .build()

        feedPagedList = LivePagedListBuilder(feedDataSourceFactory, config).build()
    }

    fun getFeedPagedList() = feedPagedList
    fun getLiveDataSource() = liveDataSource
}