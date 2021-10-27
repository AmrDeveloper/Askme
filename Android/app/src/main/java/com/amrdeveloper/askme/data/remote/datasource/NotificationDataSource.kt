package com.amrdeveloper.askme.data.remote.datasource

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.amrdeveloper.askme.data.Notification
import com.amrdeveloper.askme.data.remote.net.DEFAULT_QUERY_PAGE_SIZE
import com.amrdeveloper.askme.data.remote.net.NotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationDataSource(private var userId: String,
                             private val token: String,
                             private val scope: CoroutineScope,
                             private val notificationService: NotificationService) :
    PageKeyedDataSource<Int, Notification>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Notification>
    ) {
        scope.launch(Dispatchers.IO){
            try {
                val notifications = notificationService.getUserNotifications(userId = userId, token = "auth $token")
                if (notifications.size == DEFAULT_QUERY_PAGE_SIZE) {
                    callback.onResult(notifications, null, 2)
                } else {
                    callback.onResult(notifications, null, null)
                }
            }catch (exception : Exception) {
                Log.d("Notification", "Invalid Request ${exception.message}")
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Notification>) {
        scope.launch(Dispatchers.IO){
            try {
                val notifications = notificationService.getUserNotifications(userId = userId, token = "auth $token")
                if (params.key > 1) {
                    callback.onResult(notifications, params.key - 1)
                } else {
                    callback.onResult(notifications, null)
                }
            }catch (exception : Exception) {
                Log.d("Notification", "Invalid Request ${exception.message}")
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Notification>) {
        scope.launch(Dispatchers.IO){
            try{
                val notifications = notificationService.getUserNotifications(userId = userId, token = "auth $token")
                if (notifications.size == DEFAULT_QUERY_PAGE_SIZE) {
                    callback.onResult(notifications, params.key + 1)
                } else {
                    callback.onResult(notifications, null)
                }
            }catch (exception : Exception) {
                Log.d("Notification", "Invalid Request ${exception.message}")
            }
        }
    }
}