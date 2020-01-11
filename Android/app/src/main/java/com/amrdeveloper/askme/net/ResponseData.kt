package com.amrdeveloper.askme.net

data class ResponseData<T>(
    val responseType: ResponseType,
    val data : T
)