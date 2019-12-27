package com.amrdeveloper.askme.models

import com.google.gson.annotations.SerializedName

data class SessionData(
    @SerializedName("id")
    var userId : String,

    @SerializedName("token")
    var authToken : String
)