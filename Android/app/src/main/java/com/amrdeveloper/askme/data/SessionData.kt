package com.amrdeveloper.askme.data

import com.google.gson.annotations.SerializedName

data class SessionData(
    @SerializedName("id")
    var userId : String,

    @SerializedName("token")
    var authToken : String,

    @SerializedName("color")
    val color : String
)