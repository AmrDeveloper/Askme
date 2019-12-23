package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.data.FollowData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FollowService {

    @POST("follows/follow")
    fun followUser(
        @Header("authorization") token: String,
        @Body followData: FollowData
    ): Call<String>

    @POST("follows/unfollow")
    fun unFollowUser(
        @Header("authorization") token: String,
        @Body followData: FollowData
    ): Call<String>

}