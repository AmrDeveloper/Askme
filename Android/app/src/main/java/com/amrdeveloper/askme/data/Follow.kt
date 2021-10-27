package com.amrdeveloper.askme.data

import com.google.gson.annotations.SerializedName

enum class Follow {
    @SerializedName("1")
    FOLLOW,

    @SerializedName("0")
    UN_FOLLOW
}