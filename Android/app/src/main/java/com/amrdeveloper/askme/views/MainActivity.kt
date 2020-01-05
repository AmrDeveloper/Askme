package com.amrdeveloper.askme.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import com.amrdeveloper.askme.*
import com.amrdeveloper.askme.databinding.ActivityMainBinding
import com.amrdeveloper.askme.extensions.notNull
import com.amrdeveloper.askme.extensions.openFragmentInto
import com.amrdeveloper.askme.extensions.str
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.utils.ShortcutUtils

import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    private var mActionBar: ActionBar? = null
    private lateinit var mMainActivity: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainActivity = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mActionBar = supportActionBar
        mMainActivity.mainNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelection)

        if(intent.action == null) {
            mMainActivity.mainNavigation.selectedItemId = R.id.navigation_home
            supportFragmentManager.openFragmentInto(R.id.viewContainers, HomeFragment())
        }else{
            if(Session.isUserLogined(this)) {
                val shortcutAction = intent.action.str()
                ShortcutUtils.executeAction(shortcutAction, mMainActivity.mainNavigation)
            }else{
                Toast.makeText(this, "No Authentication", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val onNavigationItemSelection =
        BottomNavigationView.OnNavigationItemSelectedListener { menu ->
            val selectedId = mMainActivity.mainNavigation.selectedItemId
            if(selectedId == menu.itemId){
                return@OnNavigationItemSelectedListener false
            }

            when (menu.itemId) {
                R.id.navigation_home -> {
                    mActionBar.notNull { it.title = "Home" }
                    supportFragmentManager.openFragmentInto(R.id.viewContainers,
                        HomeFragment()
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    mActionBar.notNull { it.title = "Notifications" }
                    supportFragmentManager.openFragmentInto(R.id.viewContainers,
                        NotificationFragment()
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_people -> {
                    mActionBar.notNull { it.title = "People" }
                    supportFragmentManager.openFragmentInto(R.id.viewContainers,
                        PeopleFragment()
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    mActionBar.notNull { it.title = "Profile" }
                    supportFragmentManager.openFragmentInto(R.id.viewContainers,
                        ProfileFragment()
                    )
                    return@OnNavigationItemSelectedListener true
                }
            }
            return@OnNavigationItemSelectedListener false
        }
}
