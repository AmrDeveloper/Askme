package com.amrdeveloper.askme.data

import com.google.gson.annotations.SerializedName

data class Answer(

    @SerializedName("id")
    val answerId : Int,

    @SerializedName("answerBody")
    val answerBody : String,

    @SerializedName("toUserId")
    val toUserId : String,

    @SerializedName("toUsername")
    val toUserName : String,

    @SerializedName("toUserAvatar")
    val toUserAvatar : String,

    @SerializedName("fromUserId")
    val fromUserId : String,

    @SerializedName("fromUsername")
    val fromUserName : String,

    @SerializedName("fromUserAvatar")
    val fromUserAvatar : String,

    @SerializedName("questionId")
    val questionId : String,

    @SerializedName("questionBody")
    val questionBody : String,

    @SerializedName("isAnonymous")
    val isAnonymously: Anonymously,

    @SerializedName("reactions")
    var reactionsNum : Int,

    @SerializedName("isReacted")
    var isReacted : Reaction,

    @SerializedName("answerdDate")
    val answerdDate : String
)