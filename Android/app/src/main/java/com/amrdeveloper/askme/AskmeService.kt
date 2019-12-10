package com.amrdeveloper.askme

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AskmeService {

    @POST("users/login")
    fun login(
        @Body userInfo: String
    ): Call<String>
}
