package com.amrdeveloper.askme.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var mRegisterActivity: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRegisterActivity = DataBindingUtil.setContentView(this, R.layout.activity_register)
    }
}
