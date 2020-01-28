package com.amrdeveloper.askme.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.models.RegisterData
import com.amrdeveloper.askme.databinding.ActivityRegisterBinding
import com.amrdeveloper.askme.di.ViewModelProviderFactory
import com.amrdeveloper.askme.extensions.*
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.viewmodels.RegisterViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class RegisterActivity : DaggerAppCompatActivity(){

    private lateinit var mRegisterViewModel : RegisterViewModel
    private lateinit var mRegisterActivity: ActivityRegisterBinding

    @Inject lateinit var providerFactory : ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRegisterActivity = DataBindingUtil.setContentView(this, R.layout.activity_register)
        mRegisterViewModel = ViewModelProviders.of(this, providerFactory).get(RegisterViewModel::class.java)

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
            if(it != null){
                Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()

                val email: String = mRegisterActivity.emailInputEdit.text.str()
                val password: String = mRegisterActivity.passInputEdit.text.str()

                Session.login(applicationContext, email, password, it)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Invalid Register", Toast.LENGTH_SHORT).show()
            }
        })
    }
}