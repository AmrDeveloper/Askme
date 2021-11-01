package com.amrdeveloper.askme.data.source.remote.net

data class ResponseData<T>(
    val responseType: ResponseType,
    val data : T
)