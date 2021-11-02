package com.amrdeveloper.askme.ui.askquestion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.data.QuestionData
import com.amrdeveloper.askme.data.ResponseType
import com.amrdeveloper.askme.data.source.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
) : ViewModel() {

    private val questionLiveData: MutableLiveData<ResponseType> = MutableLiveData()

    fun askNewQuestion(token: String, questionData: QuestionData) {
        viewModelScope.launch {
            val result = questionRepository.createNewQuestion(token, questionData)
            if (result.isSuccess) {
                val response = result.getOrNull()
                when (response?.code()) {
                    200 -> questionLiveData.postValue(ResponseType.SUCCESS)
                    401 -> questionLiveData.postValue(ResponseType.NO_AUTH)
                    else -> questionLiveData.postValue(ResponseType.FAILURE)
                }
            } else {
                questionLiveData.postValue(ResponseType.FAILURE)
            }
        }
    }

    fun getQuestionLiveData() = questionLiveData
}