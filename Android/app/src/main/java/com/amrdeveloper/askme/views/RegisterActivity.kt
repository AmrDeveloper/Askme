package com.amrdeveloper.askme.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.contracts.RegisterContract
import com.amrdeveloper.askme.data.RegisterData
import com.amrdeveloper.askme.presenters.RegisterPresenter
import com.amrdeveloper.askme.databinding.ActivityRegisterBinding
import com.amrdeveloper.askme.events.RegisterFailureEvent
import com.amrdeveloper.askme.events.RegisterSuccessEvent
import com.amrdeveloper.askme.extensions.*
import com.amrdeveloper.askme.utils.AskmeActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class RegisterActivity : AskmeActivity(), RegisterContract.View {

    private lateinit var mRegisterActivity: ActivityRegisterBinding
    private lateinit var mRegisterPresenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRegisterActivity = DataBindingUtil.setContentView(this, R.layout.activity_register)
        mRegisterPresenter = RegisterPresenter(this)

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

            mRegisterPresenter.makeRegisterRequest(registerData)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRegisterSuccessEvent(event: RegisterSuccessEvent) {
        Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRegisterFailureEvent(event: RegisterFailureEvent) {
        Toast.makeText(this, "Invalid Register", Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar() {
        mRegisterActivity.loadingBar.show()
    }

    override fun hideProgressBar() {
        mRegisterActivity.loadingBar.gone()
    }

    override fun onNetworkOn() {
        mRegisterActivity.registerButton.clickable()
    }

    override fun onNetworkOff() {
        mRegisterActivity.registerButton.unClickable()
    }
}