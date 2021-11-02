package com.amrdeveloper.askme.data.source.remote

import com.amrdeveloper.askme.data.FollowData
import com.amrdeveloper.askme.data.source.FollowDataSource
import com.amrdeveloper.askme.data.source.remote.service.FollowService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class FollowRemoteDataSource @Inject constructor (
    private val followService: FollowService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FollowDataSource {

    override suspend fun followUser(
        token: String,
        followData: FollowData
    ): Result<Response<String>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(followService.followUser(token, followData))
        } catch (e : Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unFollowUser(
        token: String,
        followData: FollowData
    ): Result<Response<String>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(followService.unFollowUser(token, followData))
        } catch (e : Exception) {
            Result.failure(e)
        }
    }
}