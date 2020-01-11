package com.amrdeveloper.askme.models

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("id")
    val id : String,

    @SerializedName("name")
    val name: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("avatar")
    val avatarUrl: String,

    @SerializedName("wallpaper")
    val wallpaperUrl: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("active")
    val active: String,

    @SerializedName("joinDate")
    val joinDate: Long,

    @SerializedName("following")
    val followingNum: Int,

    @SerializedName("followers")
    val followersNum: Int,

    @SerializedName("questions")
    val questionsNum: Int,

    @SerializedName("answers")
    val answersNum: Int,

    @SerializedName("likes")
    val reactionsNum: Int,

    @SerializedName("color")
    val userColor : ThemeColor,

    @SerializedName("isFollow")
    val isUserFollow : Follow
)