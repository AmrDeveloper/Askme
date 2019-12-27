package com.amrdeveloper.askme.viewmodels

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.amrdeveloper.askme.models.Feed
import com.amrdeveloper.askme.extensions.notNull
import com.amrdeveloper.askme.net.AskmeClient
import com.amrdeveloper.askme.net.DEFAULT_QUERY_COUNT
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedDataSource(var userId: String) : PageKeyedDataSource<Int, Feed>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Feed>
    ) {
        AskmeClient.getFeedService().getUserFeed(userId = userId)
            .enqueue(object : Callback<List<Feed>> {
                override fun onResponse(call: Call<List<Feed>>, response: Response<List<Feed>>) {
                    val feedList = response.body()
                    feedList.notNull {
                        val size = feedList?.size
                        if (size == DEFAULT_QUERY_COUNT) {
                            callback.onResult(feedList, null, 1)
                        } else {
                            callback.onResult(feedList!!, null, 0)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Feed>>, t: Throwable) {
                    Log.d("Feed", "Invalid Request")
                }
            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Feed>) {
        AskmeClient.getFeedService().getUserFeed(userId = userId, offset = params.key)
            .enqueue(object : Callback<List<Feed>> {
                override fun onResponse(call: Call<List<Feed>>, response: Response<List<Feed>>) {
                    val feedList = response.body()
                    feedList.notNull {
                        if (params.key > 1) {
                            callback.onResult(feedList!!, params.key - 1)
                        } else {
                            callback.onResult(feedList!!, null)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Feed>>, t: Throwable) {
                    Log.d("Feed", "Invalid Request")
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Feed>) {
        AskmeClient.getFeedService().getUserFeed(userId = userId, offset = params.key)
            .enqueue(object : Callback<List<Feed>> {
                override fun onResponse(call: Call<List<Feed>>, response: Response<List<Feed>>) {
                    val feedList = response.body()
                    feedList.notNull {
                        val size = feedList?.size
                        if (size == DEFAULT_QUERY_COUNT) {
                            callback.onResult(feedList, params.key + 1)
                        } else {
                            callback.onResult(feedList!!, null)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Feed>>, t: Throwable) {
                    Log.d("Feed", "Invalid Request")
                }
            })
    }
}