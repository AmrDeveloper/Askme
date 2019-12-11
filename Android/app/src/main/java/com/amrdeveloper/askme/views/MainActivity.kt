package com.amrdeveloper.askme.views

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.databinding.ActivityMainBinding
import com.amrdeveloper.askme.utils.AskmeActivity
import com.amrdeveloper.extensions.extensions.notNull
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AskmeActivity() {

    private var mActionBar: ActionBar? = null
    private lateinit var mMainActivity: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainActivity = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mActionBar = supportActionBar
        mMainActivity.mainNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelection)
    }

    override fun onNetworkOn() {

    }

    override fun onNetworkOff() {

    }

    private val onNavigationItemSelection =
        BottomNavigationView.OnNavigationItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.navigation_home -> {
                    mActionBar.notNull { it.title = "Home" }
                    true
                }
                R.id.navigation_notifications -> {
                    mActionBar.notNull { it.title = "Notifications" }
                    true
                }
                R.id.navigation_people -> {
                    mActionBar.notNull { it.title = "People" }
                    true
                }
                R.id.navigation_profile -> {
                    mActionBar.notNull { it.title = "Profile" }
                    true
                }
            }
            false
        }
}
