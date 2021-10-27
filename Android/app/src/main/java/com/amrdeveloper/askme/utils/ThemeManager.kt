package com.amrdeveloper.askme.utils

import android.app.Activity

object ThemeManager {

    fun setUserTheme(activity : Activity){
        val color = Session.getUserColor(activity)
        activity.setTheme(ThemeFactory.getThemeStyle(color))
    }
}