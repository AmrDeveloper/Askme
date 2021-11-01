package com.amrdeveloper.askme.data.source.remote.service

import com.amrdeveloper.askme.data.*
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
    suspend fun getUserList(
        @Query("page") page: Int = DEFAULT_QUERY_PAGE_NUM,
        @Query("page_size") page_size : Int = DEFAULT_QUERY_PAGE_SIZE
    ): List<User>

    @GET("users/search")
    suspend fun getUsersSearch(
        @Query("q") query: String,
        @Query("page") page : Int = DEFAULT_QUERY_PAGE_NUM,
        @Query("page_size") page_size : Int = DEFAULT_QUERY_PAGE_SIZE
    ): List<User>

    @Multipart
    @PUT("users/avatar")
    suspend fun updateUserAvatar(
        @Part("email") requestBody: RequestBody,
        @Part avatar : MultipartBody.Part
    ): Response<String>

    @Multipart
    @PUT("users/wallpaper")
    suspend fun updateUserWallpaper(
        @Part("email") request: RequestBody,
        @Part wallpaper : MultipartBody.Part
    ): Response<String>

    @PUT("users/status")
    suspend fun updateUserStatus(
        @Header("authorization") token : String,
        @Body statusBody: StatusBody
    ) : Response<String>

    @PUT("users/address")
    suspend fun updateUserAddress(
        @Header("authorization") token : String,
        @Body locationBody: LocationBody
    ) : Response<String>

    @PUT("users/color")
    suspend fun updateUserColor(
        @Header("authorization") token : String,
        @Body colorBody: ColorBody
    ) : Response<String>

    @PUT("users/password")
    suspend fun updateUserPassword(
        @Header("authorization") token : String,
        @Body passwordBody: PasswordBody
    ) : Response<String>
}
