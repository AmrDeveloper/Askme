package com.amrdeveloper.askme.data.remote.net

import com.amrdeveloper.askme.data.LoginData
import com.amrdeveloper.askme.data.RegisterData
import com.amrdeveloper.askme.data.SessionData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    @POST("users/login")
    @Headers("content-type: application/json")
    suspend fun login(@Body body: LoginData): Response<SessionData>

    @POST("users/register")
    @Headers("content-type: application/json")
    suspend fun register(@Body body: RegisterData): Response<SessionData>

}