package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.models.AnswerData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AnswerService {

    @POST("answers/")
    suspend fun answerOneQuestion(
        @Header("authorization") token: String,
        @Body answerData: AnswerData
    ): Response<String>
}