package com.amrdeveloper.askme.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.models.Feed
import com.amrdeveloper.askme.models.Follow
import com.amrdeveloper.askme.models.FollowData
import com.amrdeveloper.askme.models.User
import com.amrdeveloper.askme.net.AskmeClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileViewModel : ViewModel() {

    private val userLiveData: MutableLiveData<User> = MutableLiveData()
    private val followLiveData : MutableLiveData<Follow> = MutableLiveData()
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

    fun followUser(token: String, followData: FollowData){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = AskmeClient.getFollowService().followUser("auth $token", followData)
                if (response.code() == 200) {
                    followLiveData.postValue(Follow.FOLLOW)
                }
            }catch (exception : Exception){
                Log.d("Follow", "Invalid Request")
            }
        }
    }

    fun unfollowUser(token: String, followData: FollowData){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = AskmeClient.getFollowService().unFollowUser("auth $token", followData)
                if (response.code() == 200) {
                    followLiveData.postValue(Follow.UN_FOLLOW)
                }
            }catch (exception : Exception){
                Log.d("Follow", "Invalid Request")
            }
        }
    }

    fun getUserLiveData() = userLiveData

    fun getFeedPagedList() = feedPagedList

    fun getFollowLiveData() = followLiveData

    private inner class FeedDataSourceFactory(var userId : String) : DataSource.Factory<Int,Feed>() {

        private val feedLiveDataSource : MutableLiveData<PageKeyedDataSource<Int, Feed>> = MutableLiveData()

        override fun create(): DataSource<Int, Feed> {
            val feedDataSource = FeedDataSource(userId, viewModelScope)
            feedLiveDataSource.postValue(feedDataSource)
            return feedDataSource
        }

        fun getFeedLiveDataSource() = feedLiveDataSource
    }
}