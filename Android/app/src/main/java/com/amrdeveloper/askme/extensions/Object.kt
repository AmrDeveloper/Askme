package com.amrdeveloper.askme.extensions

fun <T : Any> T?.notNull(f: (it: T) -> Unit) {
    if (this != null) f(this)
}

fun <T : Any> T?.str() : String {
    return this.toString()
}

fun String.isNullString() : Boolean{
    return this == "null"
}