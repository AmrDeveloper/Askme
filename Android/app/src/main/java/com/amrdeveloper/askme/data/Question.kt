package com.amrdeveloper.askme.data

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("id")
    val id : Int,

    @SerializedName("title")
    val title : String,

    @SerializedName("toUserId")
    val toUserId : String,

    @SerializedName("toUserName")
    val toUserName : String,

    @SerializedName("fromUserId")
    val fromUserId : String,

    @SerializedName("fromUserName")
    val fromUserName : String,

    @SerializedName("fromUserAvatar")
    val fromUserAvatar : String,

    @SerializedName("anonymous")
    val anonymously: Anonymously,

    @SerializedName("askedDate")
    val askedDate : String
)