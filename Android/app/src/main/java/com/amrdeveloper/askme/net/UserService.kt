package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.data.RegisterData
import com.amrdeveloper.askme.data.LoginData
import com.amrdeveloper.askme.data.SessionData
import com.amrdeveloper.askme.data.User
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @POST("users/login")
    @Headers("content-type: application/json")
    fun login(@Body body: LoginData): Call<SessionData>

    @POST("users/register")
    @Headers("content-type: application/json")
    fun register(@Body body: RegisterData) : Call<String>

    @GET("users/{email}")
    fun getUserByEmail(@Path("email") email : String) : Call<User>
}
