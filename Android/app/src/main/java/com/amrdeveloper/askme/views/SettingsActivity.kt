package com.amrdeveloper.askme.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.amrdeveloper.askme.adapter.ColorGridAdapter
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.utils.ThemeManager
import com.amrdeveloper.askme.viewmodels.SettingsViewModel
import com.amrdeveloper.askme.databinding.ActivitySettingsBinding
import com.amrdeveloper.askme.di.ViewModelProviderFactory
import com.amrdeveloper.askme.extensions.str
import com.amrdeveloper.askme.models.Constants
import com.amrdeveloper.askme.net.ResponseType
import com.amrdeveloper.askme.models.themeList
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.utils.Validation
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class SettingsActivity : DaggerAppCompatActivity() , SharedPreferences.OnSharedPreferenceChangeListener{

    private lateinit var mSettingsViewModel: SettingsViewModel
    @Inject lateinit var providerFactory: ViewModelProviderFactory
    private lateinit var mSettingsActivityBinding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.setUserTheme(this)
        super.onCreate(savedInstanceState)

        mSettingsActivityBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_settings
        )
        mSettingsViewModel = ViewModelProviders.of(this, providerFactory).get(SettingsViewModel::class.java)

        mSettingsViewModel.getStatusLiveData().observe(this, Observer {
            when(it){
                ResponseType.SUCCESS -> {
                    Toast.makeText(this, "Status Changed", Toast.LENGTH_SHORT).show()
                }
                ResponseType.FAILURE -> {
                    Toast.makeText(this, "Can't change Status", Toast.LENGTH_SHORT).show()
                }
            }
        })

        mSettingsViewModel.getLocationLiveData().observe(this, Observer {
            when(it){
                ResponseType.SUCCESS -> {
                    Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT).show()
                }
                ResponseType.FAILURE -> {
                    Toast.makeText(this, "Can't change Location", Toast.LENGTH_SHORT).show()
                }
            }
        })

        mSettingsViewModel.getColorLiveData().observe(this, Observer {
            when(it.responseType){
                ResponseType.SUCCESS -> {
                    Session.updateColor(this, it.data)
                    Toast.makeText(this, "Color Changed", Toast.LENGTH_SHORT).show()
                }
                ResponseType.FAILURE -> {
                    Toast.makeText(this, "Can't change Color", Toast.LENGTH_SHORT).show()
                }
            }
        })

        mSettingsViewModel.getPasswordLiveData().observe(this, Observer {
            when(it.responseType){
                ResponseType.SUCCESS -> {
                    Session.updatePassword(this, it.data)
                    Toast.makeText(this, "Color Password", Toast.LENGTH_SHORT).show()
                }
                ResponseType.FAILURE -> {
                    Toast.makeText(this, "Can't change Password", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun changeStatusAction(v: View) {
        val dialogView = layoutInflater.inflate(R.layout.change_status_dialog, null)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Change Status")
        dialogBuilder.setCancelable(true)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val changeButton: Button = dialogView.findViewById(R.id.changeButton)
        val statusEdit : EditText = dialogView.findViewById(R.id.statusEdit)

        changeButton.setOnClickListener {
            val statusTxt = statusEdit.text.str()
            if(statusTxt.isEmpty()){
                Toast.makeText(applicationContext, "Invalid Status", Toast.LENGTH_SHORT)
                    .show()
            }else{
                Log.d("UPDATE", "Start Request")
                val token = Session.getHeaderToken(this).str()
                val userId = Session.getUserId(this).str()
                mSettingsViewModel.changeUserStatus(token, userId, statusTxt)
            }

            alertDialog.dismiss()
        }
        dialogBuilder.show()
    }

    fun changeLocationAction(v: View) {
        val dialogView = layoutInflater.inflate(R.layout.change_location_dialog, null)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Change Location")
        dialogBuilder.setCancelable(true)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val changeButton: Button = dialogView.findViewById(R.id.changeButton)
        val locationEdit : EditText = dialogView.findViewById(R.id.locationEdit)

        changeButton.setOnClickListener {
            val locationTxt = locationEdit.text.str()
            if(locationTxt.isEmpty()){
                Toast.makeText(applicationContext, "Invalid Location", Toast.LENGTH_SHORT)
                    .show()
            }else{
                val token = Session.getHeaderToken(this).str()
                val userId = Session.getUserId(this).str()
                mSettingsViewModel.changeUserLocation(token, userId, locationTxt)
            }

            alertDialog.dismiss()
        }
        dialogBuilder.show()
    }

    fun changeColorAction(v: View) {
        val dialogView = layoutInflater.inflate(R.layout.change_color_dialog, null)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Change App Theme")
        dialogBuilder.setCancelable(true)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val colorGridView : GridView = dialogView.findViewById(R.id.colorGridView)

        val colorGridAdapter =
            ColorGridAdapter(this, themeList)
        colorGridView.adapter = colorGridAdapter

        colorGridView.setOnItemClickListener {_, _, position, _ ->
            val themeName = colorGridAdapter.getItem(position)!!.themeColor

            val token = Session.getHeaderToken(this).str()
            val userId = Session.getUserId(this).str()

            mSettingsViewModel.changeUserColor(token, userId, themeName)

            alertDialog.cancel()
        }

        dialogBuilder.show()
    }

    fun changePasswordAction(v: View) {
        val dialogView = layoutInflater.inflate(R.layout.change_password_dialog, null)

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Change Password")
        dialogBuilder.setCancelable(true)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val changeButton: Button = dialogView.findViewById(R.id.changeButton)
        val oldPassword: EditText = dialogView.findViewById(R.id.oldPasswordEdit)
        val newPassword: EditText = dialogView.findViewById(R.id.newPasswordEdit)

        changeButton.setOnClickListener {
            val oldPasswordTxt = oldPassword.text.str()
            if (Session.getUserPassword(this) == oldPasswordTxt) {
                val newPasswordTxt = newPassword.text.str()
                if(oldPasswordTxt == newPasswordTxt || Validation.isValidPassword(newPasswordTxt).not()){
                    Toast.makeText(applicationContext, "New password is equal old password", Toast.LENGTH_SHORT)
                        .show()
                }else{
                    val token = Session.getHeaderToken(this).str()
                    val userId = Session.getUserId(this).str()
                    mSettingsViewModel.changeUserPassword(token, userId, newPasswordTxt)
                }
            } else {
                Toast.makeText(applicationContext, "Invalid Old Password", Toast.LENGTH_SHORT)
                    .show()
            }
            alertDialog.dismiss()
        }

        dialogBuilder.show()
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