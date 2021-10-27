package com.amrdeveloper.askme.data.remote.datasource

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.amrdeveloper.askme.data.Feed
import com.amrdeveloper.askme.data.remote.net.DEFAULT_QUERY_PAGE_SIZE
import com.amrdeveloper.askme.data.remote.net.FeedService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeDataSource(
                     val userId : String,
                     private val scope: CoroutineScope,
                     private val feedService: FeedService) :
    PageKeyedDataSource<Int, Feed>(){

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Feed>
    ) {
        scope.launch(Dispatchers.IO){
            try {
                val feedList = feedService.getHomeFeed(userId = userId)
                if (feedList.size == DEFAULT_QUERY_PAGE_SIZE) {
                    callback.onResult(feedList, null, 2)
                } else {
                    callback.onResult(feedList, null, null)
                }
            }catch (exception : Exception){
                Log.d("Feed", "Invalid Request")
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Feed>) {
        scope.launch(Dispatchers.IO){
            try {
                val feedList = feedService.getHomeFeed(userId = userId,page = params.key)
                if (params.key > 1) {
                    callback.onResult(feedList, params.key - 1)
                } else {
                    callback.onResult(feedList, null)
                }
            }catch (exception : Exception){
                Log.d("Feed", "Invalid Request")
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Feed>) {
        scope.launch(Dispatchers.IO){
            try {
                val feedList = feedService.getHomeFeed(userId = userId,page = params.key)
                if (feedList.size == DEFAULT_QUERY_PAGE_SIZE) {
                    callback.onResult(feedList, params.key + 1)
                } else {
                    callback.onResult(feedList, null)
                }
            }catch (exception : Exception){
                Log.d("Feed", "Invalid Request")
            }
        }
    }
}