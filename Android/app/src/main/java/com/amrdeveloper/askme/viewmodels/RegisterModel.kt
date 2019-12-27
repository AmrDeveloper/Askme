package com.amrdeveloper.askme.viewmodels

import com.amrdeveloper.askme.contracts.RegisterContract
import com.amrdeveloper.askme.models.RegisterData
import com.amrdeveloper.askme.events.RegisterFailureEvent
import com.amrdeveloper.askme.events.RegisterSuccessEvent
import com.amrdeveloper.askme.net.AskmeClient
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class RegisterModel : RegisterContract.Model {

    override fun isValidInformation(registerData: RegisterData): Boolean {
        //Dummy validation
        return true
    }

    override fun makeRegisterRequest(registerData: RegisterData) {
        AskmeClient.getUserService().register(registerData).enqueue(object :
            retrofit2.Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                EventBus.getDefault().post(RegisterFailureEvent("Invalid Request"))
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                when (response.code()) {
                    200 -> {
                        EventBus.getDefault().post(RegisterSuccessEvent())
                    }
                    400 -> {
                        EventBus.getDefault().post(RegisterFailureEvent(response.body().toString()))
                    }
                    else -> {
                        EventBus.getDefault().post(RegisterFailureEvent("Invalid Request"))
                    }
                }
            }
        })
    }
}