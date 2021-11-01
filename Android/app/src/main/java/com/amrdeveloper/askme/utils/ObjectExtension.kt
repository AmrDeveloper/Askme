package com.amrdeveloper.askme.utils

import com.amrdeveloper.askme.data.remote.net.API_SERVER_URL

fun String.toServerImageUrl() : String{
    return API_SERVER_URL + this.replace("\\", "/")
}
