package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.data.LoginData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AskmeService {

    @POST("v1/users/login")
    @Headers("content-type: application/json")
    fun login(@Body body: LoginData): Call<String>
}
