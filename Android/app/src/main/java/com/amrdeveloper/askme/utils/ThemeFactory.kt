package com.amrdeveloper.askme.utils

import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.models.ThemeColor

object ThemeFactory {

    fun getThemeStyle(themeColor: ThemeColor) : Int{
        return when(themeColor){
            ThemeColor.BLACK -> R.style.BlackTheme
            ThemeColor.ORANGE -> R.style.AppTheme
            ThemeColor.BLUE -> R.style.BlueTheme
            ThemeColor.RED -> R.style.RedTheme
            ThemeColor.GREEN -> R.style.GreenTheme
        }
    }
}