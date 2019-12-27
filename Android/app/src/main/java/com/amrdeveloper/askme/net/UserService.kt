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
    fun register(@Body body: RegisterData): Call<String>

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: String,
        @Query("userId") userId : String
    ): User

    @GET("users/")
    fun getUsersQuery(
        @Query("count") count: Int = DEFAULT_QUERY_COUNT,
        @Query("offset") offset: Int = DEFAULT_QUERY_OFFSET
    ): Call<List<User>>

    @GET("users/search")
    fun getUsersSearch(
        @Query("q") query : String,
        @Query("count") count: Int = DEFAULT_QUERY_COUNT,
        @Query("offset") offset: Int = DEFAULT_QUERY_OFFSET
    ): Call<List<User>>
}
