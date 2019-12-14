package com.amrdeveloper.askme.models

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.amrdeveloper.askme.data.Feed

class FeedDataSourceFactory(var userId : String) : DataSource.Factory<Int,Feed>() {

    private val feedLiveDataSource : MutableLiveData<PageKeyedDataSource<Int, Feed>> = MutableLiveData()

    override fun create(): DataSource<Int, Feed> {
        return FeedDataSource(userId)
    }

    fun getFeedLiveDataSource() = feedLiveDataSource
}