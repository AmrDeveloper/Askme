package com.amrdeveloper.askme.views

import android.content.Intent
import android.os.Bundle

import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.amrdeveloper.askme.contracts.LoginContract
import com.amrdeveloper.askme.presenters.LoginPresenter
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.models.LoginData
import com.amrdeveloper.askme.databinding.ActivityLoginBinding
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.events.LoginFailureEvent
import com.amrdeveloper.askme.events.LoginSuccessEvent
import com.amrdeveloper.askme.extensions.clickable
import com.amrdeveloper.askme.utils.AskmeActivity
import com.amrdeveloper.askme.extensions.gone
import com.amrdeveloper.askme.extensions.show
import com.amrdeveloper.askme.extensions.unClickable
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LoginActivity : AskmeActivity(), LoginContract.View {

    private lateinit var mLoginActivity: ActivityLoginBinding
    private lateinit var mLoginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLoginActivity = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mLoginPresenter = LoginPresenter(this)

        mLoginActivity.registerTxt.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        mLoginActivity.loginButton.setOnClickListener {
            val email: String = mLoginActivity.emailInputEdit.text.toString()
            val password: String = mLoginActivity.passInputEdit.text.toString()
            val loginData = LoginData(email, password)
            mLoginPresenter.makeLoginRequest(loginData)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccessEvent(event: LoginSuccessEvent) {
        val session = Session()
        session.login(applicationContext, event.id, event.email, event.password, event.token)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginFailureEvent(event: LoginFailureEvent) {
        Toast.makeText(applicationContext, "Invalid Login", Toast.LENGTH_SHORT).show()
    }

    override fun showEmailErrorMessage() {
        mLoginActivity.emailInputLayout.error = "Invalid Email"
    }

    override fun showPasswordErrorMessage() {
        mLoginActivity.passInputLayout.error = "Invalid Password"
    }

    override fun showProgressBar() {
        mLoginActivity.loadingBar.show()
    }

    override fun hideProgressBar() {
        mLoginActivity.loadingBar.gone()
    }

    override fun onNetworkOn() {
        mLoginActivity.loginButton.clickable()
    }

    override fun onNetworkOff() {
        mLoginActivity.loginButton.unClickable()
    }
}

