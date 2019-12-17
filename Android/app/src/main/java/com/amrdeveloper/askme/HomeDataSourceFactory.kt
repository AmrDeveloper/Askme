package com.amrdeveloper.askme

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.amrdeveloper.askme.data.Feed

class HomeDataSourceFactory(var userId : String) : DataSource.Factory<Int, Feed>() {

    private val homeLiveDataSource : MutableLiveData<PageKeyedDataSource<Int, Feed>> = MutableLiveData()

    override fun create(): DataSource<Int, Feed> {
        val feedDataSource = HomeDataSource(userId)
        homeLiveDataSource.postValue(feedDataSource)
        return feedDataSource
    }

    fun getHomeLiveDataSource() = homeLiveDataSource
}