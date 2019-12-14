package com.amrdeveloper.askme.models

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.amrdeveloper.askme.data.Notification
import com.amrdeveloper.askme.extensions.notNull
import com.amrdeveloper.askme.net.AskmeClient
import com.amrdeveloper.askme.net.DEFAULT_QUERY_COUNT
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationDataSource(private var userId: String, private val token: String) :
    PageKeyedDataSource<Int, Notification>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Notification>
    ) {
        AskmeClient.getNotificationService()
            .getUserNotifications(userId = userId, token = "auth $token")
            .enqueue(object : Callback<List<Notification>> {
                override fun onResponse(
                    call: Call<List<Notification>>,
                    response: Response<List<Notification>>
                ) {
                    val notiList = response.body()
                    notiList.notNull {
                        val size = notiList?.size
                        if (size == DEFAULT_QUERY_COUNT) {
                            callback.onResult(notiList, null, 1)
                        } else {
                            callback.onResult(notiList!!, null, 0)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
                    Log.d("Notification", "Invalid Request")
                }
            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Notification>) {
        AskmeClient.getNotificationService()
            .getUserNotifications(userId = userId, token = "auth $token")
            .enqueue(object : Callback<List<Notification>> {
                override fun onResponse(
                    call: Call<List<Notification>>,
                    response: Response<List<Notification>>
                ) {
                    val notiList = response.body()
                    notiList.notNull {
                        if (params.key > 1) {
                            callback.onResult(notiList!!, params.key - 1)
                        } else {
                            callback.onResult(notiList!!, null)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
                    Log.d("Notification", "Invalid Request")
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Notification>) {
        AskmeClient.getNotificationService()
            .getUserNotifications(userId = userId, offset = params.key, token = "auth $token")
            .enqueue(object : Callback<List<Notification>> {
                override fun onResponse(
                    call: Call<List<Notification>>,
                    response: Response<List<Notification>>
                ) {
                    val notifList = response.body()
                    notifList.notNull {
                        val size = notifList?.size
                        if (size == DEFAULT_QUERY_COUNT) {
                            callback.onResult(notifList, params.key + 1)
                        } else {
                            callback.onResult(notifList!!, null)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
                    Log.d("Notification", "Invalid Request")
                }
            })
    }
}