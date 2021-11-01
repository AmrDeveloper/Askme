package com.amrdeveloper.askme.data.source

import com.amrdeveloper.askme.data.Feed

interface FeedDataSource {

    suspend fun getUserFeed(id : String, userId : String, page : Int) : Result<List<Feed>>

    suspend fun getHomeFeed(userId : String, page : Int) : Result<List<Feed>>
}