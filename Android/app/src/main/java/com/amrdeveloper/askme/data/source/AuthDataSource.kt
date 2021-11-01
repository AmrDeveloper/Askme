package com.amrdeveloper.askme.data.source

import com.amrdeveloper.askme.data.LoginData
import com.amrdeveloper.askme.data.RegisterData
import com.amrdeveloper.askme.data.SessionData
import retrofit2.Response

interface AuthDataSource {

    suspend fun login(body : LoginData) : Result<Response<SessionData>>

    suspend fun register(body : RegisterData) : Result<Response<SessionData>>

}