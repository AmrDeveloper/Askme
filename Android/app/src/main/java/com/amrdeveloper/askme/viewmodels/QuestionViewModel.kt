package com.amrdeveloper.askme.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.models.QuestionData
import com.amrdeveloper.askme.net.QuestionService
import com.amrdeveloper.askme.net.ResponseType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class QuestionViewModel @Inject constructor(private val questionService: QuestionService): ViewModel(){

    private val questionLiveData : MutableLiveData<ResponseType> = MutableLiveData()

    fun askNewQuestion(token : String, questionData: QuestionData){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = questionService.createNewQuestion(token, questionData)
                when {
                    response.code() == 200 -> {
                        questionLiveData.postValue(ResponseType.SUCCESS)
                    }
                    response.code() == 401 -> {
                        questionLiveData.postValue(ResponseType.NO_AUTH)
                    }
                    else -> {
                        questionLiveData.postValue(ResponseType.FAILURE)
                    }
                }
            }catch (exception : Exception){
                questionLiveData.postValue(ResponseType.FAILURE)
            }
        }
    }

    fun getQuestionLiveData() = questionLiveData
}