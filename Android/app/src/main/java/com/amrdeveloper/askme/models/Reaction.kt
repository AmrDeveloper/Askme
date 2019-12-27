package com.amrdeveloper.askme.models

import com.google.gson.annotations.SerializedName

enum class Reaction {
    @SerializedName("1")
    REACATED,

    @SerializedName("0")
    UN_REACATED
}