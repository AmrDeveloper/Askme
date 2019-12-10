package com.amrdeveloper.askme.data

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("name")
    private val name: String,

    @SerializedName("username")
    private val username: String,

    @SerializedName("email")
    private val email: String,

    @SerializedName("avatar")
    private val avatarUrl: String,

    @SerializedName("wallpaper")
    private val wallpaperUrl: String,

    @SerializedName("address")
    private val address: String,

    @SerializedName("status")
    private val status: String,

    @SerializedName("active")
    private val active: String,

    @SerializedName("joinDate")
    private val joinDate: String,

    @SerializedName("following")
    private val followingNum: Int,

    @SerializedName("followers")
    private val followersNum: Int,

    @SerializedName("questions")
    private val questionsNum: Int,

    @SerializedName("answers")
    private val answersNum: Int,

    @SerializedName("likes")
    private val reactionsNum: Int
)