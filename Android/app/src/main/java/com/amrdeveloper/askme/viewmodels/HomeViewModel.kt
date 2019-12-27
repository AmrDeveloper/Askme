package com.amrdeveloper.askme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.models.Feed

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

    private inner class HomeDataSourceFactory(var userId : String) : DataSource.Factory<Int, Feed>() {

        private val homeLiveDataSource : MutableLiveData<PageKeyedDataSource<Int, Feed>> = MutableLiveData()

        override fun create(): DataSource<Int, Feed> {
            val feedDataSource = HomeDataSource(userId, viewModelScope)
            homeLiveDataSource.postValue(feedDataSource)
            return feedDataSource
        }

        fun getHomeLiveDataSource() = homeLiveDataSource
    }
}