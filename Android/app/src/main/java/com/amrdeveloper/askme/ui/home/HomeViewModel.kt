package com.amrdeveloper.askme.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.amrdeveloper.askme.R
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

    private val _messages = MutableLiveData<Int>()
    val messages : LiveData<Int> = _messages

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
            if (result.isSuccess && result.getOrNull()?.code() == 200) {
                callback()
            } else {
                _messages.value = R.string.error_react
            }
        }
    }

    fun unreactAnswer(token : String, reactionData: ReactionData, callback : () -> Unit){
        viewModelScope.launch {
            val result = reactionRepository.deleteAnswerReaction(token, reactionData)
            if (result.isSuccess && result.getOrNull()?.code() == 200) {
                callback()
            } else {
                _messages.value = R.string.error_unreact
            }
        }
    }

    fun getFeedPagedList() = homePagedList
}