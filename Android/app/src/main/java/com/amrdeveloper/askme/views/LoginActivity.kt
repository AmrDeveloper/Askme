package com.amrdeveloper.askme.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.amrdeveloper.askme.contracts.LoginContract
import com.amrdeveloper.askme.presenters.LoginPresenter
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.LoginData
import com.amrdeveloper.askme.databinding.ActivityLoginBinding
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.events.LoginFailureEvent
import com.amrdeveloper.askme.events.LoginSuccessEvent
import com.amrdeveloper.extensions.extensions.gone
import com.amrdeveloper.extensions.extensions.show
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.EventBus

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var mLoginActivity: ActivityLoginBinding
    private lateinit var mLoginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLoginActivity = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mLoginPresenter = LoginPresenter(this)

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
        session.login(applicationContext, event.email, event.password, event.token)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginFailureEvent(event: LoginFailureEvent) {
        Toast.makeText(applicationContext, "Invalid Login", Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar() {
        mLoginActivity.loadingBar.show()
    }

    override fun hideProgressBar() {
        mLoginActivity.loadingBar.gone()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}

