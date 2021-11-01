package com.amrdeveloper.askme.ui.questionanswer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.data.Answer
import com.amrdeveloper.askme.data.Reaction
import com.amrdeveloper.askme.data.ReactionData
import com.amrdeveloper.askme.data.source.remote.service.AnswerService
import com.amrdeveloper.askme.data.source.remote.service.ReactionService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuestionAnswerViewModel @Inject constructor(private val answerService: AnswerService,
                                                  private val reactionService: ReactionService) : ViewModel(){

    private val answerLiveData : MutableLiveData<Answer> = MutableLiveData()

    fun getQuestionAnswer(token : String, answerId : String, userId : String){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val answer = answerService.getQuestionAnswer(token, answerId, userId)
                answerLiveData.postValue(answer)
            }catch (exception : Exception){
                Log.d("QuestionAnswerViewModel", "Question Answer Invalid Request")
            }
        }
    }

    fun reactAnswer(token : String, reactionData: ReactionData){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = reactionService.createAnswerReaction(token, reactionData)
                if(response.code() == 200){
                    withContext(Dispatchers.Main){
                        val currentAnswer = answerLiveData.value
                        currentAnswer?.isReacted = Reaction.UN_REACATED
                        answerLiveData.postValue(currentAnswer)
                    }
                }
            }catch (exception : Exception){
                Log.d("QuestionAnswerViewModel","Invalid React ${exception.message}")
            }
        }
    }

    fun unreactAnswer(token : String, reactionData: ReactionData){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = reactionService.deleteAnswerReaction(token, reactionData)
                if(response.code() == 200){
                    withContext(Dispatchers.Main){
                        val currentAnswer = answerLiveData.value
                        currentAnswer?.isReacted = Reaction.REACATED
                        answerLiveData.postValue(currentAnswer)
                    }
                }
            }catch (exception : Exception){
                Log.d("QuestionAnswerViewModel","Invalid unreact ${exception.message}")
            }
        }
    }

    fun getAnswerLiveData() = answerLiveData
}