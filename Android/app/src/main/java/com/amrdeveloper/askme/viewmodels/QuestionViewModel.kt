package com.amrdeveloper.askme.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.models.QuestionData
import com.amrdeveloper.askme.net.AskmeClient
import com.amrdeveloper.askme.net.ResponseType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class QuestionViewModel @Inject constructor(): ViewModel(){

    private val questionLiveData : MutableLiveData<ResponseType> = MutableLiveData()

    fun askNewQuestion(token : String, questionData: QuestionData){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = AskmeClient.getQuestionService().createNewQuestion(token, questionData)
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