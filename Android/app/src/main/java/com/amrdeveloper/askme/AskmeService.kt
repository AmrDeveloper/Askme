package com.amrdeveloper.askme

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AskmeService {

    @POST("/login")
    fun login(
        @Body email: String,
        @Body password: String
    ): Call<String>

    
}
