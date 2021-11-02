package com.amrdeveloper.askme.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.amrdeveloper.askme.data.Feed
import com.amrdeveloper.askme.data.ReactionData
import com.amrdeveloper.askme.data.source.FeedDataSource
import com.amrdeveloper.askme.data.source.ReactionDataSource
import com.amrdeveloper.askme.data.source.remote.paging.HomePagingDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val feedDataSource: FeedDataSource,
    private val reactionDataSource: ReactionDataSource
) : ViewModel() {

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
        viewModelScope.launch {
            val result = reactionDataSource.createAnswerReaction(token, reactionData)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.code() == 200) {
                    callback()
                }
            } else {
                // TODO : Add Error handler in UI
            }
        }
    }

    fun unreactAnswer(token : String, reactionData: ReactionData, callback : () -> Unit){
        viewModelScope.launch {
            val result = reactionDataSource.deleteAnswerReaction(token, reactionData)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.code() == 200) {
                    callback()
                }
            } else {
                // TODO : Add Error handler in UI
            }
        }
    }

    fun getFeedPagedList() = homePagedList
}