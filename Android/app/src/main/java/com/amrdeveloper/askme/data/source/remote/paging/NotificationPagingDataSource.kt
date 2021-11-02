package com.amrdeveloper.askme.data.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.amrdeveloper.askme.data.Notification
import com.amrdeveloper.askme.data.source.NotificationDataSource
import com.amrdeveloper.askme.data.source.NotificationRepository
import retrofit2.HttpException
import java.io.IOException

class NotificationPagingDataSource(
    private val notificationDataSource: NotificationDataSource,
    private val token: String,
    private var userId: String
) : PagingSource<Int, Notification>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        val position = params.key ?: 0
        return try {
            val response = notificationDataSource.getUserNotifications(token, userId, position)
            val feed = response.getOrDefault(listOf())
            val nextKey = if (feed.isEmpty()) null else position + 1
            val prevKey = if (position == 0) null else position - 1
            LoadResult.Page(feed, prevKey, nextKey)
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}