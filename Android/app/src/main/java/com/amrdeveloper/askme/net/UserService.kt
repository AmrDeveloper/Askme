package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: String,
        @Query("userId") userId: String
    ): User

    @GET("users/")
    suspend fun getUsersQuery(
        @Query("count") count: Int = DEFAULT_QUERY_COUNT,
        @Query("offset") offset: Int = DEFAULT_QUERY_OFFSET
    ): List<User>

    @GET("users/search")
    suspend fun getUsersSearch(
        @Query("q") query: String,
        @Query("count") count: Int = DEFAULT_QUERY_COUNT,
        @Query("offset") offset: Int = DEFAULT_QUERY_OFFSET
    ): List<User>

    @Multipart
    @PUT("users/avatar")
    suspend fun updateUserAvatar(
        @Part("email") email: RequestBody,
        @Part avatar : MultipartBody.Part
    ): Response<String>

    @Multipart
    @PUT("users/wallpaper")
    suspend fun updateUserWallpaper(
        @Part("email") email: RequestBody,
        @Part wallpaper : MultipartBody.Part
    ): Response<String>
}
