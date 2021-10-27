package com.amrdeveloper.askme.extensions

import com.amrdeveloper.askme.data.remote.net.API_SERVER_URL

fun <T : Any> T?.notNull(f: (it: T) -> Unit) {
    if (this != null) f(this)
}

fun <T : Any> T?.str() : String {
    return this.toString()
}

fun String.isNullString() : Boolean{
    return this == "null"
}

fun String.toServerImageUrl() : String{
    return API_SERVER_URL + this.replace("\\", "/")
}
