package com.amrdeveloper.askme.data

import com.google.gson.annotations.SerializedName

data class AnswerData(
    @SerializedName("questionId")
    val questionId : String,

    @SerializedName("body")
    val questionBody : String,

    @SerializedName("fromUser")
    val fromUser : String,

    @SerializedName("toUser")
    val toUser : String
)