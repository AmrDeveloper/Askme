package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.models.Feed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedService {

    @GET("feeds/")
    fun getUserFeed(
        @Query("id") userId: String,
        @Query("offset") offset: Int = DEFAULT_QUERY_OFFSET,
        @Query("count") count: Int = DEFAULT_QUERY_COUNT
    ): Call<List<Feed>>

    @GET("feeds/home")
    fun getHomeFeed(
        @Query("id") userId: String,
        @Query("offset") offset: Int = DEFAULT_QUERY_OFFSET,
        @Query("count") count: Int = DEFAULT_QUERY_COUNT
    ): Call<List<Feed>>
}