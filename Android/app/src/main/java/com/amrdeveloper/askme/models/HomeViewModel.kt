package com.amrdeveloper.askme.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.data.Feed

class HomeViewModel : ViewModel(){

    companion object{

        private lateinit var userId: String

        fun setUserId(id: String) {
            userId = id
        }
    }

    private var homePagedList: LiveData<PagedList<Feed>>
    private var liveDataSource: LiveData<PageKeyedDataSource<Int, Feed>>

    init {
        val homeDataSourceFactory =
            HomeDataSourceFactory(userId)
        liveDataSource = homeDataSourceFactory.getHomeLiveDataSource()

        val config: PagedList.Config =
            PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .build()

        homePagedList = LivePagedListBuilder(homeDataSourceFactory, config).build()
    }

    fun getFeedPagedList() = homePagedList
    fun getLiveDataSource() = liveDataSource
}