package com.amrdeveloper.askme.ui.questionanswer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.Answer
import com.amrdeveloper.askme.data.Reaction
import com.amrdeveloper.askme.data.ReactionData
import com.amrdeveloper.askme.data.source.AnswerRepository
import com.amrdeveloper.askme.data.source.ReactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionAnswerViewModel @Inject constructor(
    private val answerRepository: AnswerRepository,
    private val reactionRepository: ReactionRepository
) : ViewModel() {

    private val answerLiveData : MutableLiveData<Answer> = MutableLiveData()

    private val _messages = MutableLiveData<Int>()
    val messages : LiveData<Int> = _messages

    fun getQuestionAnswer(token : String, answerId : String, userId : String){
        viewModelScope.launch {
            val result = answerRepository.getQuestionAnswer(token, answerId, userId)
            if (result.isSuccess) {
                answerLiveData.postValue(result.getOrNull())
            } else {
                _messages.value = R.string.error_get_question_answer
            }
        }
    }

    fun reactAnswer(token : String, reactionData: ReactionData){
        viewModelScope.launch {
            val result = reactionRepository.createAnswerReaction(token, reactionData)
            if (result.isSuccess && result.getOrNull()?.code() == 200) {
                val currentAnswer = answerLiveData.value
                currentAnswer?.isReacted = Reaction.UN_REACATED
                answerLiveData.postValue(currentAnswer)
            } else {
                _messages.value = R.string.error_react
            }
        }
    }

    fun unreactAnswer(token : String, reactionData: ReactionData){
        viewModelScope.launch {
            val result = reactionRepository.deleteAnswerReaction(token, reactionData)
            if (result.isSuccess && result.getOrNull()?.code() == 200) {
                val currentAnswer = answerLiveData.value
                currentAnswer?.isReacted = Reaction.REACATED
                answerLiveData.postValue(currentAnswer)
            } else {
                _messages.value = R.string.error_unreact
            }
        }
    }

    fun getAnswerLiveData() = answerLiveData
}