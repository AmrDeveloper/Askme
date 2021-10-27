package com.amrdeveloper.askme.ui.answerquestion

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.data.AnswerData
import com.amrdeveloper.askme.data.Question
import com.amrdeveloper.askme.data.remote.net.AnswerService
import com.amrdeveloper.askme.data.remote.net.QuestionService
import com.amrdeveloper.askme.data.remote.net.ResponseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AnswerViewModel"

@HiltViewModel
class AnswerQuestionViewModel @Inject constructor(private val questionService: QuestionService,
                                                  private val answerService: AnswerService) : ViewModel(){

    private val questionLiveData : MutableLiveData<Question> = MutableLiveData()
    private val answerLiveData : MutableLiveData<ResponseType> = MutableLiveData()

    fun getQuestionById(token : String, id : String){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val question = questionService.getQuestionById(token, id)
                questionLiveData.postValue(question)
            }catch (exception : Exception){
                Log.d(TAG,"Invalid Answer Request")
            }
        }
    }

    fun answerQuestion(token : String, answerData: AnswerData){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = answerService.answerOneQuestion(token, answerData)
                when(response.code()){
                    200 -> answerLiveData.postValue(ResponseType.SUCCESS)
                    401 -> answerLiveData.postValue(ResponseType.NO_AUTH)
                    else -> answerLiveData.postValue(ResponseType.FAILURE)
                }
            }catch (exception : Exception){
                answerLiveData.postValue(ResponseType.FAILURE)
                Log.d(TAG,"Invalid Answer Request ${exception.message}")
            }
        }
    }

    fun getQuestinLiveData() = questionLiveData
    fun getAnswerLiveData() = answerLiveData
}