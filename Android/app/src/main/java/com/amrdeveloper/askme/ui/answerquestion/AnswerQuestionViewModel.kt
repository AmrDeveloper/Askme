package com.amrdeveloper.askme.ui.answerquestion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.data.AnswerData
import com.amrdeveloper.askme.data.Question
import com.amrdeveloper.askme.data.ResponseType
import com.amrdeveloper.askme.data.source.AnswerDataSource
import com.amrdeveloper.askme.data.source.QuestionDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnswerQuestionViewModel @Inject constructor(
    private val questionDataSource: QuestionDataSource,
    private val answerDataSource: AnswerDataSource
) : ViewModel() {

    private val questionLiveData : MutableLiveData<Question> = MutableLiveData()
    private val answerLiveData : MutableLiveData<ResponseType> = MutableLiveData()

    fun getQuestionById(token : String, id : String){
        viewModelScope.launch {
            val result = questionDataSource.getQuestionById(token, id)
            if (result.isSuccess) {
                questionLiveData.postValue(result.getOrNull())
            }
        }
    }

    fun answerQuestion(token : String, answerData: AnswerData){
        viewModelScope.launch {
            val result = answerDataSource.answerOneQuestion(token, answerData)
            if (result.isSuccess) {
                val response = result.getOrNull()
                when(response?.code()){
                    200 -> answerLiveData.postValue(ResponseType.SUCCESS)
                    401 -> answerLiveData.postValue(ResponseType.NO_AUTH)
                    else -> answerLiveData.postValue(ResponseType.FAILURE)
                }
            } else {
                answerLiveData.postValue(ResponseType.FAILURE)
            }
        }
    }

    fun getQuestionLiveData() = questionLiveData
    fun getAnswerLiveData() = answerLiveData
}