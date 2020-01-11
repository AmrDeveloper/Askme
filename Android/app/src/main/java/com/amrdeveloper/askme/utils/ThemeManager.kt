package com.amrdeveloper.askme.utils

import android.app.Activity
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.utils.ThemeFactory

object ThemeManager {

    fun setUserTheme(activity : Activity){
        val color = Session.getUserColor(activity)
        activity.setTheme(ThemeFactory.getThemeStyle(color))
    }
}