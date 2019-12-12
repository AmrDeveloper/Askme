package com.amrdeveloper.askme.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.contracts.RegisterContract
import com.amrdeveloper.askme.presenters.RegisterPresenter
import com.amrdeveloper.askme.databinding.ActivityRegisterBinding
import com.amrdeveloper.askme.events.RegisterFailureEvent
import com.amrdeveloper.askme.events.RegisterSuccessEvent
import com.amrdeveloper.askme.extensions.clickable
import com.amrdeveloper.askme.utils.AskmeActivity
import com.amrdeveloper.askme.extensions.gone
import com.amrdeveloper.askme.extensions.show
import com.amrdeveloper.askme.extensions.unClickable
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