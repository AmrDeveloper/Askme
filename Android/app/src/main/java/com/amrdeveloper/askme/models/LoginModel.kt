package com.amrdeveloper.askme.models

import com.amrdeveloper.askme.AskmeClient
import com.amrdeveloper.askme.contracts.LoginContract
import com.amrdeveloper.askme.events.LoginFailureEvent
import com.amrdeveloper.askme.events.LoginSuccessEvent
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginModel : LoginContract.Model {

    override fun isValidInformation(email: String, pass: String): Boolean {
        return email.isNotEmpty() && pass.isNotEmpty()
    }

    override fun makeLoginRequest(email: String, pass: String) {
        val requestBody = createJsonBody(email, pass)
        AskmeClient.getAskmeService().login(requestBody)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    EventBus.getDefault().post(LoginFailureEvent())
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.code() == 200) {
                        val token = response.body().toString()
                        EventBus.getDefault().post(LoginSuccessEvent(email, pass, token))
                    } else {
                        EventBus.getDefault().post(LoginFailureEvent())
                    }
                }
            })
    }

    private fun createJsonBody(email: String, pass: String): String {
        val userInfoJson = JSONObject()
        userInfoJson.put("email", email)
        userInfoJson.put("password", pass)
        return userInfoJson.toString()
    }
}