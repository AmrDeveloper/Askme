package com.amrdeveloper.askme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.adapter.FeedAdapter
import com.amrdeveloper.askme.models.Feed
import com.amrdeveloper.askme.models.ReactionData
import com.amrdeveloper.askme.net.FeedService
import com.amrdeveloper.askme.net.PagingConfig
import com.amrdeveloper.askme.net.ReactionService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import javax.inject.Inject

class HomeViewModel @Inject constructor(private val feedService: FeedService,
                                        private val reactionService: ReactionService): ViewModel(){

    private var homePagedList: LiveData<PagedList<Feed>> = MutableLiveData()
    private lateinit var liveDataSource: LiveData<PageKeyedDataSource<Int, Feed>>

    fun loadUserHomeFeed(id : String){
        val homeDataSourceFactory = HomeDataSourceFactory(id)
        liveDataSource = homeDataSourceFactory.getHomeLiveDataSource()

        homePagedList = LivePagedListBuilder(homeDataSourceFactory, PagingConfig.getConfig()).build()
    }

    fun reactAnswer(token : String, reactionData: ReactionData, callback: FeedAdapter.Callback){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = reactionService.createAnswerReaction(token, reactionData)
                if(response.code() == 200){
                    callback.onCallback(true)
                }else{
                    callback.onCallback(false)
                }
            }catch (exception : Exception){
                callback.onCallback(false)
            }
        }
    }

    fun unreactAnswer(token : String, reactionData: ReactionData, callback: FeedAdapter.Callback){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = reactionService.deleteAnswerReaction(token, reactionData)
                if(response.code() == 200){
                    callback.onCallback(true)
                }else{
                    callback.onCallback(false)
                }
            }catch (exception : Exception){
                callback.onCallback(false)
            }
        }
    }

    fun getFeedPagedList() = homePagedList

    private inner class HomeDataSourceFactory(var userId : String) : DataSource.Factory<Int, Feed>() {

        private val homeLiveDataSource : MutableLiveData<PageKeyedDataSource<Int, Feed>> = MutableLiveData()

        override fun create(): DataSource<Int, Feed> {
            val feedDataSource = HomeDataSource(userId, viewModelScope, feedService)
            homeLiveDataSource.postValue(feedDataSource)
            return feedDataSource
        }

        fun getHomeLiveDataSource() = homeLiveDataSource
    }
}