package com.amrdeveloper.askme.models

import com.amrdeveloper.askme.utils.Validation
import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)
{
    fun isValidEmail() : Boolean{
        return Validation.isValidEmail(email)
    }

    fun isValidPassword() : Boolean{
        return Validation.isValidPassword(password)
    }
}