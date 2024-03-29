package com.amrdeveloper.askme.data

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("id")
    val id: Int,

    @SerializedName("body")
    val body: String,

    @SerializedName("createdDate")
    val createdDate: Long,

    @SerializedName("action")
    val action: Action,

    @SerializedName("data")
    val data: String,

    @SerializedName("opened")
    var isOpened: Open
)