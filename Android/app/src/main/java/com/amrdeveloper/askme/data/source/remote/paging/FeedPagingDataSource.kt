package com.amrdeveloper.askme.data.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.amrdeveloper.askme.data.Feed
import com.amrdeveloper.askme.data.source.FeedDataSource
import retrofit2.HttpException
import java.io.IOException

class FeedPagingDataSource(
    private val feedService: FeedDataSource,
    private var id: String,
    private var userId: String,
) : PagingSource<Int, Feed>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Feed> {
        val position = params.key ?: 0
        return try {
            val response = feedService.getUserFeed(id, userId, position)
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

    override fun getRefreshKey(state: PagingState<Int, Feed>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}