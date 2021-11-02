package com.amrdeveloper.askme.data.source

import com.amrdeveloper.askme.data.FollowData
import retrofit2.Response
import javax.inject.Inject

class FollowRepository @Inject constructor(private val followDataSource: FollowDataSource){

    suspend fun followUser(token : String, followData: FollowData) : Result<Response<String>> {
        return followDataSource.followUser(token, followData)
    }

    suspend fun unFollowUser(token : String, followData: FollowData) : Result<Response<String>> {
        return followDataSource.unFollowUser(token, followData)
    }
}