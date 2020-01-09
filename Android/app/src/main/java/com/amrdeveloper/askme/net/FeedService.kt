package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.models.Feed
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedService {

    @GET("feeds/")
    suspend fun getUserFeed(
        @Query("id") id: String,
        @Query("userId") userId : String = "",
        @Query("offset") offset: Int = DEFAULT_QUERY_OFFSET,
        @Query("count") count: Int = DEFAULT_QUERY_COUNT
    ): List<Feed>

    @GET("feeds/home")
    suspend fun getHomeFeed(
        @Query("id") userId: String,
        @Query("offset") offset: Int = DEFAULT_QUERY_OFFSET,
        @Query("count") count: Int = DEFAULT_QUERY_COUNT
    ): List<Feed>
}