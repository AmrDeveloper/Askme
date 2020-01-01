package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.models.Question
import com.amrdeveloper.askme.models.QuestionData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface QuestionService {

    @POST("questions/")
    suspend fun createNewQuestion(
        @Header("authorization") token: String,
        @Body question: QuestionData
    ): Response<String>

    @GET("questions/{id}")
    suspend fun getQuestionById(
        @Header("authorization") token: String,
        @Path("id") questionId : String
    ): Question
}