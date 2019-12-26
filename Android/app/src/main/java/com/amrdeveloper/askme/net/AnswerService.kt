package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.data.AnswerData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AnswerService {

    @POST("answers/")
    fun answerOneQuestion(
        @Header("authorization") token: String,
        @Body answerData: AnswerData
    ): Call<String>
}