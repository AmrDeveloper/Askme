package com.amrdeveloper.askme.data.source.remote

import com.amrdeveloper.askme.data.*
import com.amrdeveloper.askme.data.source.UserDataSource
import com.amrdeveloper.askme.data.source.remote.net.UserService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userService: UserService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {

    override suspend fun getUserById(id: String, userId: String): Result<User> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.success(userService.getUserById(id, userId))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getUserList(page: Int): Result<List<User>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(userService.getUserList(page))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsersSearch(query: String, page: Int): Result<List<User>> = withContext(ioDispatcher) {
            return@withContext try {
                Result.success(userService.getUsersSearch(query, page))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun updateUserAvatar(
        requestBody: RequestBody,
        avatar: MultipartBody.Part): Result<Response<String>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(userService.updateUserAvatar(requestBody, avatar))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserWallpaper(
        requestBody: RequestBody,
        wallpaper: MultipartBody.Part): Result<Response<String>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(userService.updateUserWallpaper(requestBody, wallpaper))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserStatus(token: String, statusBody: StatusBody): Result<Response<String>> = withContext(ioDispatcher) {
            return@withContext try {
                Result.success(userService.updateUserStatus(token, statusBody))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun updateUserAddress(
        token: String,
        locationBody: LocationBody): Result<Response<String>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(userService.updateUserAddress(token, locationBody))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserColor(token: String, colorBody: ColorBody): Result<Response<String>> = withContext(ioDispatcher) {
            return@withContext try {
                Result.success(userService.updateUserColor(token, colorBody))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun updateUserPassword(
        token: String,
        passwordBody: PasswordBody
    ): Result<Response<String>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(userService.updateUserPassword(token, passwordBody))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}