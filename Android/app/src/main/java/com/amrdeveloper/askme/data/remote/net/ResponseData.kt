package com.amrdeveloper.askme.data.remote.net

data class ResponseData<T>(
    val responseType: ResponseType,
    val data : T
)