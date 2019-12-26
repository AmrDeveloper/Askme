package com.amrdeveloper.askme.data

import com.google.gson.annotations.SerializedName

enum class Action{
    @SerializedName("follow")
    FOLLOW,

    @SerializedName("question")
    QUESTION,

    @SerializedName("answer")
    ANSWER
}