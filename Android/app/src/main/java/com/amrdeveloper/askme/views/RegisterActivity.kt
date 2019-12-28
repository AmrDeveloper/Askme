package com.amrdeveloper.askme.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.models.RegisterData
import com.amrdeveloper.askme.databinding.ActivityRegisterBinding
import com.amrdeveloper.askme.extensions.*
import com.amrdeveloper.askme.viewmodels.RegisterViewModel

class RegisterActivity : AppCompatActivity(){

    private lateinit var mRegisterViewModel : RegisterViewModel
    private lateinit var mRegisterActivity: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRegisterActivity = DataBindingUtil.setContentView(this, R.layout.activity_register)
        mRegisterViewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)

        mRegisterActivity.loginTxt.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        mRegisterActivity.registerButton.setOnClickListener {
            val name = mRegisterActivity.nameInputEdit.text.str()
            val email = mRegisterActivity.emailInputEdit.text.str()
            val username = mRegisterActivity.usernameInputEdit.text.str()
            val password = mRegisterActivity.passInputEdit.text.str()

            val registerData = RegisterData(name, email, username , password)

            if(registerData.isValidRegisterInfo()){
                mRegisterViewModel.userRegister(registerData)
                mRegisterActivity.loadingBar.show()
            }else{
                Toast.makeText(this, "Invalid Information", Toast.LENGTH_SHORT).show()
            }
        }

        mRegisterViewModel.getRegisterLiveData().observe(this, Observer {
            mRegisterActivity.loadingBar.gone()
            if(it == "valid"){
                Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Invalid Register", Toast.LENGTH_SHORT).show()
            }
        })
    }
}