package com.amrdeveloper.askme.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.data.Feed

class HomeViewModel : ViewModel(){

    private var homePagedList: LiveData<PagedList<Feed>> = MutableLiveData()
    private lateinit var liveDataSource: LiveData<PageKeyedDataSource<Int, Feed>>

    fun loadUserHomeFeed(id : String){
        val homeDataSourceFactory = HomeDataSourceFactory(id)
        liveDataSource = homeDataSourceFactory.getHomeLiveDataSource()

        val config: PagedList.Config =
            PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .build()

        homePagedList = LivePagedListBuilder(homeDataSourceFactory, config).build()
    }

    fun getFeedPagedList() = homePagedList
}