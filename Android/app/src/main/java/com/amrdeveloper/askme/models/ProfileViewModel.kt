package com.amrdeveloper.askme.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.data.Feed
import com.amrdeveloper.askme.data.User
import com.amrdeveloper.askme.extensions.notNull
import com.amrdeveloper.askme.net.AskmeClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "ProfileViewModel"

class ProfileViewModel : ViewModel() {

    private val userLiveData: MutableLiveData<User> = MutableLiveData()
    private var feedPagedList: LiveData<PagedList<Feed>> = MutableLiveData()
    private lateinit var liveDataSource: LiveData<PageKeyedDataSource<Int, Feed>>

    fun loadUserFeed(userId : String){
        val feedDataSourceFactory = FeedDataSourceFactory(userId)
        liveDataSource = feedDataSourceFactory.getFeedLiveDataSource()

        val config: PagedList.Config =
            PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .build()

        feedPagedList = LivePagedListBuilder(feedDataSourceFactory, config).build()
    }

    fun loadUserInformation(userId : String, localId : String){
        AskmeClient.getUserService().getUserByEmail(userId, localId)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    response.body().notNull {
                        userLiveData.postValue(it)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d(TAG, "Load Information Failure")
                }
            })
    }

    fun getUserLiveData() : LiveData<User>{
        return userLiveData
    }

    fun getFeedPagedList() : LiveData<PagedList<Feed>>{
        return feedPagedList
    }
}