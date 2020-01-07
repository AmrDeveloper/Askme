package com.amrdeveloper.askme.utils

import android.util.Patterns

object Validation {

    private const val NAME_MIN_LENGTH = 5
    private const val PASSWORD_MIN_LENGTH = 8
    private val NAME_PATTERN_REGEX = Regex("[a-zA-Z0-9_-]{5,}")

    fun isValidName(name : String) : Boolean{
        if(name.length < NAME_MIN_LENGTH){
            return false
        }
        return name.matches(NAME_PATTERN_REGEX)
    }

    fun isValidUsername(username : String) : Boolean{
        if(username.length < NAME_MIN_LENGTH){
            return false
        }
        return username.matches(NAME_PATTERN_REGEX)
    }

    fun isValidEmail(email : String) : Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password : String) : Boolean {
        return password.length >= PASSWORD_MIN_LENGTH
    }
}

