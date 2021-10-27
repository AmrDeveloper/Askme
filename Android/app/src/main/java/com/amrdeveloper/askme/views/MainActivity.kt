package com.amrdeveloper.askme.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation.findNavController
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.databinding.ActivityMainBinding
import com.amrdeveloper.askme.extensions.str
import com.amrdeveloper.askme.models.Constants
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.utils.ShortcutUtils
import com.amrdeveloper.askme.utils.ThemeManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener{

    private var supportedActionBar: ActionBar? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.setUserTheme(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        supportedActionBar = supportActionBar
        binding.mainNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelection)

        if (intent.action == null) {
            binding.mainNavigation.selectedItemId = R.id.navigation_home
            findNavController(this,R.id.viewContainers).navigate(R.id.homeFragment)
        } else {
            if (Session.isUserLogined(this)) {
                val shortcutAction = intent.action.str()
                ShortcutUtils.executeAction(shortcutAction, binding.mainNavigation)
            } else {
                Toast.makeText(this, "No Authentication", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val onNavigationItemSelection =
        BottomNavigationView.OnNavigationItemSelectedListener { menu ->
            val selectedId = binding.mainNavigation.selectedItemId
            if(selectedId == menu.itemId){
                return@OnNavigationItemSelectedListener false
            }

            when (menu.itemId) {
                R.id.navigation_home -> {
                    supportedActionBar?.let { it.title = "Home" }
                    findNavController(this,R.id.viewContainers).navigate(R.id.homeFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    supportedActionBar?.let { it.title = "Notifications" }
                    findNavController(this,R.id.viewContainers).navigate(R.id.notificationFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_people -> {
                    supportedActionBar?.let { it.title = "People" }
                    findNavController(this,R.id.viewContainers).navigate(R.id.peopleFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    supportedActionBar?.let { it.title = "Profile" }
                    findNavController(this,R.id.viewContainers).navigate(R.id.profileFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            return@OnNavigationItemSelectedListener false
        }

    override fun onStart() {
        super.onStart()
        getSharedPreferences(Constants.SESSION_PREFERENCE, Context.MODE_PRIVATE)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        getSharedPreferences(Constants.SESSION_PREFERENCE, Context.MODE_PRIVATE)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, key: String?) {
        if (key == Constants.COLOR) {
            intent.action = "INTENT_CHANGE_THEME"
            recreate()
        }
    }
}
