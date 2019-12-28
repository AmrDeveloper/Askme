package com.amrdeveloper.askme.utils

import android.util.Patterns

object Validation {

    private const val PASSWORD_MIN_LENGTH = 8

    fun isValidEmail(email : String) : Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password : String) : Boolean {
        return password.length >= PASSWORD_MIN_LENGTH
    }
}

