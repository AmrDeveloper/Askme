package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.data.Feed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedService {

    @GET("feeds/")
    fun getUserFeed(
        @Query("id") userId: String,
        @Query("offset") offset: Int = 0,
        @Query("count") count: Int = 25
    ): Call<Feed>
}