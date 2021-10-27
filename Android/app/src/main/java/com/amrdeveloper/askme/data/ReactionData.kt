package com.amrdeveloper.askme.data

import com.google.gson.annotations.SerializedName

data class ReactionData(
    @SerializedName("fromUser")
    val fromUser : String,

    @SerializedName("toUser")
    val toUser : String,

    @SerializedName("answerId")
    val answerId : String,

    @SerializedName("reactionsType")
    val reactionType : Int = 0
)