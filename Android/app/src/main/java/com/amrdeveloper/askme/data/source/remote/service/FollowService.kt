package com.amrdeveloper.askme.data.source.remote.service

import com.amrdeveloper.askme.data.FollowData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FollowService {

    @POST("follows/follow")
    suspend fun followUser(
        @Header("authorization") token: String,
        @Body followData: FollowData
    ): Response<String>

    @POST("follows/unfollow")
    suspend fun unFollowUser(
        @Header("authorization") token: String,
        @Body followData: FollowData
    ): Response<String>
}