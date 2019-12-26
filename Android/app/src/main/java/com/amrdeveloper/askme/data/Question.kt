package com.amrdeveloper.askme.data

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("id")
    val id : Int,

    @SerializedName("title")
    val title : String,

    @SerializedName("toUser")
    val toUser : String,

    @SerializedName("fromUser")
    val fromUser : String,

    @SerializedName("fromUserAvatar")
    val fromUserAvatar : String,

    @SerializedName("anonymous")
    val anonymously: Anonymously,

    @SerializedName("askedDate")
    val askedDate : String
)