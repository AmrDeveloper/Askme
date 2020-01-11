package com.amrdeveloper.askme.models

import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import com.amrdeveloper.askme.models.ThemeColor

class Theme(
    @ColorRes val colorValue : Int,
    @StyleRes val colorStyle : Int,
    val themeColor : ThemeColor
)