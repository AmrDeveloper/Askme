package com.amrdeveloper.askme.data.source

import com.amrdeveloper.askme.data.Question
import com.amrdeveloper.askme.data.QuestionData
import retrofit2.Response

interface QuestionDataSource {

    suspend fun createNewQuestion(token : String, question: QuestionData) : Result<Response<String>>

    suspend fun getQuestionById(token : String, questionID : String) : Result<Question>
}