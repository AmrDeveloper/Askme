package com.amrdeveloper.askme.utils

import android.app.Activity
import android.view.Menu

class MenuManager(
    private val menu: Menu?,
    private val activity: Activity
) {

    fun setMenuItems(id: Int) {
        clearMenuItems()
        activity.menuInflater.inflate(id, menu)
    }

    fun clearMenuItems() {
        menu?.clear()
    }
}