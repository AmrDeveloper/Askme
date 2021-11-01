package com.amrdeveloper.askme.data.source.remote

import com.amrdeveloper.askme.data.Notification
import com.amrdeveloper.askme.data.source.NotificationDataSource
import com.amrdeveloper.askme.data.source.remote.net.NotificationService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class NotificationRemoteDataSource(
    private val notificationService: NotificationService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : NotificationDataSource {

    override suspend fun getUserNotifications(token : String, userId : String, page : Int) : Result<List<Notification>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(notificationService.getUserNotifications(token, userId, page))
        } catch (e : Exception) {
            Result.failure(e)
        }
    }

    override suspend fun makeNotificationOpened(token : String, id : String) : Result<Response<String>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(notificationService.makeNotificationOpened(token, id))
        } catch (e : Exception) {
            Result.failure(e)
        }
    }
}