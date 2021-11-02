package com.amrdeveloper.askme.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.amrdeveloper.askme.data.*
import com.amrdeveloper.askme.data.source.remote.paging.UserPagingDataSource
import com.amrdeveloper.askme.data.source.remote.paging.UserPagingSearchDataSource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDataSource: UserDataSource) {

    suspend fun getUserById(id : String, userId : String) : Result<User> {
        return userDataSource.getUserById(id, userId)
    }

    fun getUserList() : Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { UserPagingDataSource(userDataSource) }).flow
    }

    fun getUsersSearch(query : String) : Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { UserPagingSearchDataSource(userDataSource, query) }).flow
    }

    suspend fun updateUserAvatar(requestBody : RequestBody, avatar : MultipartBody.Part) : Result<Response<String>> {
        return userDataSource.updateUserAvatar(requestBody, avatar)
    }

    suspend fun updateUserWallpaper(requestBody : RequestBody, wallpaper : MultipartBody.Part) : Result<Response<String>> {
        return userDataSource.updateUserWallpaper(requestBody, wallpaper)
    }

    suspend fun updateUserStatus(token : String, statusBody: StatusBody) : Result<Response<String>> {
        return userDataSource.updateUserStatus(token, statusBody)
    }

    suspend fun updateUserAddress(token : String, locationBody: LocationBody) : Result<Response<String>> {
        return userDataSource.updateUserAddress(token, locationBody)
    }

    suspend fun updateUserColor(token : String, colorBody: ColorBody) : Result<Response<String>> {
        return userDataSource.updateUserColor(token, colorBody)
    }

    suspend fun updateUserPassword(token : String, passwordBody: PasswordBody) : Result<Response<String>> {
        return userDataSource.updateUserPassword(token, passwordBody)
    }
}