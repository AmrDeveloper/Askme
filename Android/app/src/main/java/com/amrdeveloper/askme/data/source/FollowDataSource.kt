package com.amrdeveloper.askme.data.source

import com.amrdeveloper.askme.data.FollowData
import retrofit2.Response

interface FollowDataSource {

    suspend fun followUser(token : String, followData: FollowData) : Result<Response<String>>

    suspend fun unFollowUser(token : String, followData: FollowData) : Result<Response<String>>
}