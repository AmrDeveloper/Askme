package com.amrdeveloper.askme.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.amrdeveloper.askme.data.Feed
import com.amrdeveloper.askme.data.ReactionData
import com.amrdeveloper.askme.data.source.FeedDataSource
import com.amrdeveloper.askme.data.source.remote.paging.HomePagingDataSource
import com.amrdeveloper.askme.data.source.remote.service.ReactionService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val feedDataSource: FeedDataSource,
                                        private val reactionService: ReactionService): ViewModel(){

    private var homePagedList: MutableLiveData<PagingData<Feed>> = MutableLiveData()

    fun loadUserHomeFeed(userId : String){
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = 10, enablePlaceholders = false),
                pagingSourceFactory = { HomePagingDataSource(feedDataSource, userId) }).flow.collect {
                    homePagedList.value = it
            }
        }
    }

    fun reactAnswer(token : String, reactionData: ReactionData, callback : () -> Unit){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = reactionService.createAnswerReaction(token, reactionData)
                if(response.code() == 200){
                    withContext(Dispatchers.Main){
                        callback()
                    }
                }
            }catch (exception : Exception){
                Log.d("HomeViewModel","Invalid React ${exception.message}")
            }
        }
    }

    fun unreactAnswer(token : String, reactionData: ReactionData, callback : () -> Unit){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = reactionService.deleteAnswerReaction(token, reactionData)
                if(response.code() == 200){
                    withContext(Dispatchers.Main){
                        callback()
                    }
                }
            }catch (exception : Exception){
                Log.d("HomeViewModel","Invalid unreact ${exception.message}")
            }
        }
    }

    fun getFeedPagedList() = homePagedList
}