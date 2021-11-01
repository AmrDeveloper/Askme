package com.amrdeveloper.askme.data

data class ResponseData<T>(
    val responseType: ResponseType,
    val data : T
)