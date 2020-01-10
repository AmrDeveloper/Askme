package com.amrdeveloper.askme.models

import com.google.gson.annotations.SerializedName

data class PasswordBody(
    @SerializedName("id")
    val id : String,

    @SerializedName("password")
    val password : String
)