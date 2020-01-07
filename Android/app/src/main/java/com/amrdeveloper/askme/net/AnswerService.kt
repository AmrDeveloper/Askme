package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.models.Answer
import com.amrdeveloper.askme.models.AnswerData
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