package com.amrdeveloper.askme.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.amrdeveloper.askme.data.Feed
import com.amrdeveloper.askme.data.ReactionData
import com.amrdeveloper.askme.data.source.FeedRepository
import com.amrdeveloper.askme.data.source.ReactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val reactionRepository: ReactionRepository
) : ViewModel() {

    private var homePagedList: MutableLiveData<PagingData<Feed>> = MutableLiveData()

    fun loadUserHomeFeed(userId : String){
        viewModelScope.launch {
            feedRepository.getHomeFeed(userId).collect {
                homePagedList.value = it
            }
        }
    }

    fun reactAnswer(token : String, reactionData: ReactionData, callback : () -> Unit){
        viewModelScope.launch {
            val result = reactionRepository.createAnswerReaction(token, reactionData)
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
            val result = reactionRepository.deleteAnswerReaction(token, reactionData)
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