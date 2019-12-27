package com.amrdeveloper.askme.models

import com.google.gson.annotations.SerializedName

enum class Action{
    @SerializedName("normal")
    NOTMAL,

    @SerializedName("follow")
    FOLLOW,

    @SerializedName("question")
    QUESTION,

    @SerializedName("answer")
    ANSWER
}