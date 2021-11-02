package com.amrdeveloper.askme.data.source

import com.amrdeveloper.askme.data.Question
import com.amrdeveloper.askme.data.QuestionData
import retrofit2.Response
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val questionDataSource: QuestionDataSource){

    suspend fun createNewQuestion(token : String, question: QuestionData) : Result<Response<String>> {
        return questionDataSource.createNewQuestion(token, question)
    }

    suspend fun getQuestionById(token : String, questionID : String) : Result<Question> {
        return questionDataSource.getQuestionById(token, questionID)
    }
}