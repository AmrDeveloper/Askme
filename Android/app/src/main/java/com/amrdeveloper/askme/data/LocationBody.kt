package com.amrdeveloper.askme.data

import com.google.gson.annotations.SerializedName

data class LocationBody(
    @SerializedName("id")
    val id : String,

    @SerializedName("address")
    val address : String
)