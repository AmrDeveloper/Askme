package com.amrdeveloper.askme.data

import com.google.gson.annotations.SerializedName

data class ReactionData(
    @SerializedName("userId")
    val userId : String,

    @SerializedName("answerId")
    val answerId : String
)