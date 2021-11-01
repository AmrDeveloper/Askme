package com.amrdeveloper.askme.data.source

import com.amrdeveloper.askme.data.Answer
import com.amrdeveloper.askme.data.AnswerData
import retrofit2.Response

interface AnswerDataSource {

    suspend fun getQuestionAnswer(token : String, id : String, userId : String) : Result<Answer>

    suspend fun answerOneQuestion(token : String, answerData: AnswerData) : Result<Response<String>>
}