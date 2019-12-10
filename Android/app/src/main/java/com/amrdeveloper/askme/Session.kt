package com.amrdeveloper.askme

import android.content.Context

class Session {

    fun login(context: Context, email: String, password: String, token: String): Boolean {
        val preferencesEditor =
            context.getSharedPreferences(Constants.SESSION_PREFERNSE, Context.MODE_PRIVATE).edit()
        preferencesEditor.putString(Constants.EMAIL, email)
        preferencesEditor.putString(Constants.PASSWORD, password)
        preferencesEditor.putString(Constants.LOGIN_TOKEN, token)
        return preferencesEditor.commit()
    }

    fun logout(context: Context): Boolean {
        val preferencesEditor =
            context.getSharedPreferences(Constants.SESSION_PREFERNSE, Context.MODE_PRIVATE).edit()
        return preferencesEditor.clear().commit()
    }

    fun getUserToken(context: Context): String? {
        val preferences =
            context.getSharedPreferences(Constants.SESSION_PREFERNSE, Context.MODE_PRIVATE)
        return preferences.getString(Constants.LOGIN_TOKEN, "")
    }

    fun getUserEmail(context: Context): String? {
        val preferences =
            context.getSharedPreferences(Constants.SESSION_PREFERNSE, Context.MODE_PRIVATE)
        return preferences.getString(Constants.EMAIL, "")
    }

    fun getUserPassword(context: Context): String? {
        val preferences =
            context.getSharedPreferences(Constants.SESSION_PREFERNSE, Context.MODE_PRIVATE)
        return preferences.getString(Constants.PASSWORD, "")
    }

    fun updateToken(context: Context, token: String): Boolean {
        val preferencesEditor =
            context.getSharedPreferences(Constants.SESSION_PREFERNSE, Context.MODE_PRIVATE).edit()
        preferencesEditor.putString(Constants.LOGIN_TOKEN, token)
        return preferencesEditor.commit()
    }

    fun updateEmail(context: Context, email: String): Boolean {
        val preferencesEditor =
            context.getSharedPreferences(Constants.SESSION_PREFERNSE, Context.MODE_PRIVATE).edit()
        preferencesEditor.putString(Constants.EMAIL, email)
        return preferencesEditor.commit()
    }

    fun updatePassword(context: Context, password: String): Boolean {
        val preferencesEditor =
            context.getSharedPreferences(Constants.SESSION_PREFERNSE, Context.MODE_PRIVATE).edit()
        preferencesEditor.putString(Constants.PASSWORD, password)
        return preferencesEditor.commit()
    }
}