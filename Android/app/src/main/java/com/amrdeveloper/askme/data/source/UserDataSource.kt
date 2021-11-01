package com.amrdeveloper.askme.data.source

import com.amrdeveloper.askme.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface UserDataSource {

    suspend fun getUserById(id : String, userId : String) : Result<User>

    suspend fun getUserList(page : Int) : Result<List<User>>

    suspend fun getUsersSearch(query : String, page : Int) : Result<List<User>>

    suspend fun updateUserAvatar(requestBody : RequestBody, avatar : MultipartBody.Part) : Result<Response<String>>

    suspend fun updateUserWallpaper(requestBody : RequestBody, wallpaper : MultipartBody.Part) : Result<Response<String>>

    suspend fun updateUserStatus(token : String, statusBody: StatusBody) : Result<Response<String>>

    suspend fun updateUserAddress(token : String, locationBody: LocationBody) : Result<Response<String>>

    suspend fun updateUserColor(token : String, colorBody: ColorBody) : Result<Response<String>>

    suspend fun updateUserPassword(token : String, passwordBody: PasswordBody) : Result<Response<String>>
}