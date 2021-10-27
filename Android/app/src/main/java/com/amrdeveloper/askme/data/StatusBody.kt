package com.amrdeveloper.askme.data

import com.google.gson.annotations.SerializedName

data class StatusBody(
    @SerializedName("id")
    val id : String,

    @SerializedName("status")
    val status : String
)