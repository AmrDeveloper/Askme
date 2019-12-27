package com.amrdeveloper.askme.viewmodels

import com.amrdeveloper.askme.net.AskmeClient
import com.amrdeveloper.askme.models.LoginData
import com.amrdeveloper.askme.contracts.LoginContract
import com.amrdeveloper.askme.models.SessionData
import com.amrdeveloper.askme.events.LoginFailureEvent
import com.amrdeveloper.askme.events.LoginSuccessEvent
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginModel : LoginContract.Model {

    override fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty()
    }

    override fun isValidPassword(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 8
    }

    override fun makeLoginRequest(loginData: LoginData) {
        AskmeClient.getUserService().login(loginData)
            .enqueue(object : Callback<SessionData> {
                override fun onResponse(call: Call<SessionData>, response: Response<SessionData>) {
                    if (response.code() == 200) {
                        val sessionData = response.body()
                        EventBus.getDefault().post(
                            LoginSuccessEvent(
                                sessionData!!.userId,
                                loginData.email,
                                loginData.password,
                                sessionData.authToken
                            )
                        )
                    } else {
                        EventBus.getDefault().post(LoginFailureEvent())
                    }
                }

                override fun onFailure(call: Call<SessionData>, t: Throwable) {
                    EventBus.getDefault().post(LoginFailureEvent())
                }
            })
    }
}