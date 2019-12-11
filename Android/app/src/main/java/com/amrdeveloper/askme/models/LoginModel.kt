package com.amrdeveloper.askme.models

import com.amrdeveloper.askme.net.AskmeClient
import com.amrdeveloper.askme.data.LoginData
import com.amrdeveloper.askme.contracts.LoginContract
import com.amrdeveloper.askme.events.LoginFailureEvent
import com.amrdeveloper.askme.events.LoginSuccessEvent
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginModel : LoginContract.Model {

    override fun isValidInformation(loginData: LoginData): Boolean {
        return loginData.email.isNotEmpty() && loginData.password.isNotEmpty()
    }

    override fun makeLoginRequest(loginData: LoginData) {
        AskmeClient.getAskmeService().login(loginData)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    EventBus.getDefault().post(LoginFailureEvent())
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.code() == 200) {
                        val token = response.body().toString()
                        EventBus.getDefault().post(LoginSuccessEvent(loginData.email, loginData.password, token))
                    } else {
                        EventBus.getDefault().post(LoginFailureEvent())
                    }
                }
            })


    }
}