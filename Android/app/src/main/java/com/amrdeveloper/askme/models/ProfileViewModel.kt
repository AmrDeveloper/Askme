package com.amrdeveloper.askme.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.data.Feed
import com.amrdeveloper.askme.data.User
import com.amrdeveloper.askme.net.AskmeClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val userLiveData: MutableLiveData<User> = MutableLiveData()
    private var feedPagedList: LiveData<PagedList<Feed>> = MutableLiveData()
    private lateinit var liveDataSource: LiveData<PageKeyedDataSource<Int, Feed>>

    fun loadUserFeed(userId : String){
        val feedDataSourceFactory = FeedDataSourceFactory(userId)
        liveDataSource = feedDataSourceFactory.getFeedLiveDataSource()

        val config: PagedList.Config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .build()

        feedPagedList = LivePagedListBuilder(feedDataSourceFactory, config).build()
    }

    fun loadUserInformation(userId : String, localId : String){
        viewModelScope.launch(Dispatchers.IO){
            val user =  AskmeClient.getUserService().getUserById(userId, localId)
            userLiveData.postValue(user)
        }
    }

    fun getUserLiveData() = userLiveData

    fun getFeedPagedList() = feedPagedList

    private inner class FeedDataSourceFactory(var userId : String) : DataSource.Factory<Int,Feed>() {

        private val feedLiveDataSource : MutableLiveData<PageKeyedDataSource<Int, Feed>> = MutableLiveData()

        override fun create(): DataSource<Int, Feed> {
            val feedDataSource = FeedDataSource(userId)
            feedLiveDataSource.postValue(feedDataSource)
            return feedDataSource
        }

        fun getFeedLiveDataSource() = feedLiveDataSource
    }
}