package com.amrdeveloper.askme.utils

import android.content.Context
import com.amrdeveloper.askme.data.*

object Session {

    fun login(context: Context, email: String, password: String, sessionData: SessionData): Boolean {
        val preferencesEditor =
            context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE).edit()
        preferencesEditor.putString(EMAIL, email)
        preferencesEditor.putString(PASSWORD, password)
        preferencesEditor.putString(USER_ID, sessionData.userId)
        preferencesEditor.putString(COLOR, sessionData.color)
        preferencesEditor.putString(LOGIN_TOKEN, sessionData.authToken)
        return preferencesEditor.commit()
    }

    fun logout(context: Context): Boolean {
        val preferencesEditor =
            context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE).edit()
        return preferencesEditor.clear().commit()
    }

    fun getUserId(context: Context): String? {
        val preferences =
            context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)
        return preferences.getString(USER_ID, "")
    }

    fun getUserToken(context: Context): String? {
        val preferences =
            context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)
        return preferences.getString(LOGIN_TOKEN, "")
    }

    fun getHeaderToken(context: Context) : String?{
        return "auth ${getUserToken(context)}"
    }

    fun getUserEmail(context: Context): String? {
        val preferences =
            context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)
        return preferences.getString(EMAIL, "")
    }

    fun getUserColor(context: Context) : ThemeColor {
        val preferences =
            context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)
        return ThemeColor.valueOf(preferences.getString(COLOR, ThemeColor.ORANGE.name).toString())
    }

    fun getUserPassword(context: Context): String? {
        val preferences =
            context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)
        return preferences.getString(PASSWORD, "")
    }

    fun updateUserId(context: Context, id: String): Boolean {
        val preferencesEditor =
            context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE).edit()
        preferencesEditor.putString(USER_ID, id)
        return preferencesEditor.commit()
    }

    fun updateToken(context: Context, token: String): Boolean {
        val preferencesEditor =
            context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE).edit()
        preferencesEditor.putString(LOGIN_TOKEN, token)
        return preferencesEditor.commit()
    }

    fun updateEmail(context: Context, email: String): Boolean {
        val preferencesEditor =
            context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE).edit()
        preferencesEditor.putString(EMAIL, email)
        return preferencesEditor.commit()
    }

    fun updatePassword(context: Context, password: String): Boolean {
        val preferencesEditor =
            context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE).edit()
        preferencesEditor.putString(PASSWORD, password)
        return preferencesEditor.commit()
    }

    fun updateColor(context: Context, themeColor : ThemeColor) : Boolean{
        val preferencesEditor =
            context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE).edit()
        preferencesEditor.putString(COLOR, themeColor.name)
        return preferencesEditor.commit()
    }

    fun isUserLogined(context: Context) : Boolean{
        val preferences =
            context.getSharedPreferences(SESSION_PREFERENCE, Context.MODE_PRIVATE)
        return preferences.getString(LOGIN_TOKEN,"").isNullOrBlank().not()
    }
}