package com.amrdeveloper.askme.data.source.remote.service

import com.amrdeveloper.askme.data.Answer
import com.amrdeveloper.askme.data.AnswerData
import retrofit2.Response
import retrofit2.http.*

interface AnswerService {

    @GET("answers/question/{id}")
    suspend fun getQuestionAnswer(
        @Header("authorization") token: String,
        @Path("id") id : String,
        @Query("userId") userId : String
    ) : Answer

    @POST("answers/")
    suspend fun answerOneQuestion(
        @Header("authorization") token: String,
        @Body answerData: AnswerData
    ): Response<String>
}