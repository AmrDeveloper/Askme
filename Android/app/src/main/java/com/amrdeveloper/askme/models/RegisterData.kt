package com.amrdeveloper.askme.models

import com.amrdeveloper.askme.utils.Validation
import com.google.gson.annotations.SerializedName

data class RegisterData(
    @SerializedName("name")
    val name : String,

    @SerializedName("email")
    val email : String,

    @SerializedName("username")
    val username : String,

    @SerializedName("password")
    val password : String
)
{
    fun isValidRegisterInfo() : Boolean{
        return isValidName()
                && isValidUsername()
                && isValidEmail()
                && isValidPassword()
    }

    fun isValidName() : Boolean{
        return true
    }

    fun isValidUsername() : Boolean{
        return true
    }

    fun isValidEmail() : Boolean{
        return Validation.isValidEmail(email)
    }

    fun isValidPassword() : Boolean{
        return Validation.isValidPassword(password)
    }
}