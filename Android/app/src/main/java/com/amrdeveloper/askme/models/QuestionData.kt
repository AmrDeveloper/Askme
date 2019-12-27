package com.amrdeveloper.askme.models

import com.google.gson.annotations.SerializedName

data class QuestionData(
    @SerializedName("title")
    var title : String,

    @SerializedName("toUser")
    val toUser: String,

    @SerializedName("fromUser")
    var fromUser : String,

    @SerializedName("anonymous")
    val anonymous : String
)