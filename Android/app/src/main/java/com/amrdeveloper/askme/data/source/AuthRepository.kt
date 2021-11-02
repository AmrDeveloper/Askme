package com.amrdeveloper.askme.data.source

import com.amrdeveloper.askme.data.LoginData
import com.amrdeveloper.askme.data.RegisterData
import com.amrdeveloper.askme.data.SessionData
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authDataSource: AuthDataSource) {

    suspend fun login(body : LoginData) : Result<Response<SessionData>> {
        return authDataSource.login(body)
    }

    suspend fun register(body : RegisterData) : Result<Response<SessionData>> {
        return authDataSource.register(body)
    }
}