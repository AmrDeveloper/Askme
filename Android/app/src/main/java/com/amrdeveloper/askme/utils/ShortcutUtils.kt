package com.amrdeveloper.askme.utils

import com.amrdeveloper.askme.R
import com.google.android.material.bottomnavigation.BottomNavigationView

object ShortcutUtils {

    private const val HOME_ACTION = "com.amrdeveloper.askme.action.home"
    private const val PEOPLE_ACTION = "com.amrdeveloper.askme.action.people"
    private const val NOTIFICATIONS_ACTION = "com.amrdeveloper.askme.action.notifications"
    private const val PROFILE_ACTION = "com.amrdeveloper.askme.action.profile"

    fun executeAction(action : String, navigation : BottomNavigationView){
        when(action){
            HOME_ACTION -> navigation.selectedItemId = R.id.navigation_home
            NOTIFICATIONS_ACTION -> navigation.selectedItemId = R.id.navigation_notifications
            PEOPLE_ACTION -> navigation.selectedItemId = R.id.navigation_people
            PROFILE_ACTION -> navigation.selectedItemId = R.id.navigation_profile
        }
    }
}