package com.amrdeveloper.askme.data.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.amrdeveloper.askme.data.User
import com.amrdeveloper.askme.data.source.UserDataSource
import com.amrdeveloper.askme.data.source.UserRepository
import retrofit2.HttpException
import java.io.IOException

class UserPagingSearchDataSource(
    private val userdataSource: UserDataSource,
    private val query: String
) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val position = params.key ?: 0
        return try {
            val response = userdataSource.getUsersSearch(query, position)
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

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}