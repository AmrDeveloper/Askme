package com.amrdeveloper.askme.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

import android.widget.Toast
import com.amrdeveloper.askme.contracts.LoginContract
import com.amrdeveloper.askme.presenters.LoginPresenter
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.Session
import com.amrdeveloper.askme.events.LoginFailureEvent
import com.amrdeveloper.askme.events.LoginSuccessEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.EventBus

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var loginButton: Button

    private lateinit var mLoginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()

        mLoginPresenter = LoginPresenter(this)

        loginButton.setOnClickListener { view ->
            val email: String = emailEditText.text.toString()
            val password: String = passwordEdit.text.toString()
            mLoginPresenter.makeLoginRequest(email, password)
        }
    }

    fun initViews() {
        emailEditText = findViewById(R.id.emailEdit)
        passwordEdit = findViewById(R.id.passwordEdit)
        loginButton = findViewById(R.id.loginButton)
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

    }

    override fun hideProgressBar() {

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

