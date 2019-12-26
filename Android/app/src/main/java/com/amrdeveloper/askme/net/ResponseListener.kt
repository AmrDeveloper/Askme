package com.amrdeveloper.askme.net

interface ResponseListener<T> {
    fun onSuccess(result: T)
    fun onFailure()
}