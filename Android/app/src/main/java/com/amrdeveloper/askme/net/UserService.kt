package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.models.RegisterData
import com.amrdeveloper.askme.models.LoginData
import com.amrdeveloper.askme.models.SessionData
import com.amrdeveloper.askme.models.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @POST("users/login")
    @Headers("content-type: application/json")
    suspend fun login(@Body body: LoginData): Response<SessionData>

    @POST("users/register")
    @Headers("content-type: application/json")
    fun register(@Body body: RegisterData): Call<String>

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: String,
        @Query("userId") userId : String
    ): User

    @GET("users/")
    suspend fun getUsersQuery(
        @Query("count") count: Int = DEFAULT_QUERY_COUNT,
        @Query("offset") offset: Int = DEFAULT_QUERY_OFFSET
    ): List<User>

    @GET("users/search")
    fun getUsersSearch(
        @Query("q") query : String,
        @Query("count") count: Int = DEFAULT_QUERY_COUNT,
        @Query("offset") offset: Int = DEFAULT_QUERY_OFFSET
    ): Call<List<User>>
}
