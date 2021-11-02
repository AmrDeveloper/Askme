package com.amrdeveloper.askme.ui.questionanswer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun getQuestionAnswer(token : String, answerId : String, userId : String){
        viewModelScope.launch {
            val result = answerRepository.getQuestionAnswer(token, answerId, userId)
            if (result.isSuccess) {
                answerLiveData.postValue(result.getOrNull())
            } else {

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

            }
        }
    }

    fun getAnswerLiveData() = answerLiveData
}