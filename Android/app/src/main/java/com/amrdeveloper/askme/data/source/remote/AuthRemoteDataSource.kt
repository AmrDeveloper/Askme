package com.amrdeveloper.askme.data.source.remote

import com.amrdeveloper.askme.data.LoginData
import com.amrdeveloper.askme.data.RegisterData
import com.amrdeveloper.askme.data.SessionData
import com.amrdeveloper.askme.data.source.AuthDataSource
import com.amrdeveloper.askme.data.source.remote.service.AuthService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception

class AuthRemoteDataSource(
    private val authService: AuthService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthDataSource {

    override suspend fun login(body: LoginData): Result<Response<SessionData>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(authService.login(body))
        } catch (e : Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(body: RegisterData):Result<Response<SessionData>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.success(authService.register(body))
        } catch (e : Exception) {
            Result.failure(e)
        }
    }

}