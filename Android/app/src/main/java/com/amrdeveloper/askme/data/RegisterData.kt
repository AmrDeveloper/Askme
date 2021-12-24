package com.amrdeveloper.askme.data

import com.google.gson.annotations.SerializedName

data class RegisterData(
    @SerializedName("name")
    val name : String,

    @SerializedName("email")
    val email : String,

    @SerializedName("username")
    val username : String,

    @SerializedName("password")
    val password : String
)