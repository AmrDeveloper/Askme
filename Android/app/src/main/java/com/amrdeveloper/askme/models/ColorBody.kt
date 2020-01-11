package com.amrdeveloper.askme.models

import com.google.gson.annotations.SerializedName

data class ColorBody(

    @SerializedName("id")
    val id: String,

    @SerializedName("color")
    val color: String
)