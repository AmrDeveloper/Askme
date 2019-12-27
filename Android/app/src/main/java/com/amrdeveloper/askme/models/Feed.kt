package com.amrdeveloper.askme.models

import com.google.gson.annotations.SerializedName

data class Feed(
    @SerializedName("answerId")
    var answerId : Int,

    @SerializedName("questionBody")
    var questionBody : String,

    @SerializedName("answerBody")
    var answerBody : String,

    @SerializedName("fromUserId")
    var fromUserId : Int,

    @SerializedName("fromUsername")
    var fromUserName : String,

    @SerializedName("fromUserEmail")
    var fromUserEmail : String,

    @SerializedName("fromUserAvatar")
    var fromUserAvatar : String,

    @SerializedName("toUserId")
    var toUserId : Int,

    @SerializedName("toUsername")
    var toUserName : String,

    @SerializedName("toUserEmail")
    var toUserEmail : String,

    @SerializedName("toUserAvatar")
    var toUserAvatar : String,

    @SerializedName("reactions")
    var reactionsNum : Int,

    @SerializedName("isReacted")
    var isReacted : Reaction,

    @SerializedName("answerDate")
    var answerDate : Long
)
