package com.amrdeveloper.askme.data

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

    private fun isValidName() : Boolean{
        return Validation.isValidName(name)
    }

    private fun isValidUsername() : Boolean{
        return Validation.isValidUsername(username)
    }

    private fun isValidEmail() : Boolean{
        return Validation.isValidEmail(email)
    }

    private fun isValidPassword() : Boolean{
        return Validation.isValidPassword(password)
    }
}