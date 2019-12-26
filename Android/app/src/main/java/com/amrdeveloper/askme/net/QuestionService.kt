package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.data.Question
import com.amrdeveloper.askme.data.QuestionData
import retrofit2.Call
import retrofit2.http.*

interface QuestionService {

    @POST("questions/")
    fun createNewQueestion(
        @Header("authorization") token: String,
        @Body question: QuestionData
    ): Call<String>

    @GET("questions/{id}")
    fun getQuestionById(
        @Header("authorization") token: String,
        @Path("id") questionId : String
    ): Call<Question>
}