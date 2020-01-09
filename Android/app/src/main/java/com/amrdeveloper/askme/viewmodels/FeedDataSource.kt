package com.amrdeveloper.askme.viewmodels

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.amrdeveloper.askme.models.Feed
import com.amrdeveloper.askme.net.DEFAULT_QUERY_COUNT
import com.amrdeveloper.askme.net.FeedService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedDataSource(var id: String,
                     var userId : String,
                     private val scope: CoroutineScope,
                     private val feedService: FeedService) :
    PageKeyedDataSource<Int, Feed>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Feed>
    ) {
        scope.launch(Dispatchers.IO){
            try{
                val feedList = feedService.getUserFeed(id, userId)
                if (feedList.size == DEFAULT_QUERY_COUNT) {
                    callback.onResult(feedList, null, 1)
                } else {
                    callback.onResult(feedList, null, 0)
                }
            }catch (exception : Exception){
                Log.d("Feed", "Invalid Request")
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Feed>) {
        scope.launch(Dispatchers.IO){
            try{
                val feedList = feedService.getUserFeed(id = id,userId = userId, offset = params.key)
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

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Feed>) {
        scope.launch(Dispatchers.IO){
            try{
                val feedList =  feedService.getUserFeed(id = id,userId = userId, offset = params.key)
                if (feedList.size == DEFAULT_QUERY_COUNT) {
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