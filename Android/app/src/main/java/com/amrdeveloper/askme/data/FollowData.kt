package com.amrdeveloper.askme.data

import com.google.gson.annotations.SerializedName

data class FollowData(
    @SerializedName("fromUser")
    val fromUser : String,

    @SerializedName("toUser")
    val toUser : String
)