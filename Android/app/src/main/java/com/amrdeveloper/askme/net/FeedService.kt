package com.amrdeveloper.askme.net

import com.amrdeveloper.askme.models.Feed
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedService {

    @GET("feeds/")
    suspend fun getUserFeed(
        @Query("id") id: String,
        @Query("userId") userId : String = "",
        @Query("page") page: Int = DEFAULT_QUERY_PAGE_NUM,
        @Query("page_size") page_size: Int = DEFAULT_QUERY_PAGE_SIZE
    ): List<Feed>

    @GET("feeds/home")
    suspend fun getHomeFeed(
        @Query("id") userId: String,
        @Query("page") page: Int = DEFAULT_QUERY_PAGE_NUM,
        @Query("page_size") page_size: Int = DEFAULT_QUERY_PAGE_SIZE
    ): List<Feed>
}