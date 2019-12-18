package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.data.QuestionData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface QuestionService {

    @POST("questions/")
    fun createNewQueestion(
        @Header("authorization") token: String,
        @Body question: QuestionData
    ): Call<String>
}