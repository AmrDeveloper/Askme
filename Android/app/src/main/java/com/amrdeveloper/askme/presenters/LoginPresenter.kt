package com.amrdeveloper.askme.presenters

import com.amrdeveloper.askme.data.LoginData
import com.amrdeveloper.askme.contracts.LoginContract
import com.amrdeveloper.askme.models.LoginModel
import com.amrdeveloper.askme.events.LoginFailureEvent
import org.greenrobot.eventbus.EventBus

class LoginPresenter(
    private val view : LoginContract.View
) : LoginContract.Presenter {

    private val model: LoginContract.Model

    init {
        model = LoginModel()
    }

    override fun makeLoginRequest(email: String, pass: String) {
        val isValidInfo : Boolean = model.isValidInformation(email, pass)
        if(isValidInfo){
            view.showProgressBar()
            model.makeLoginRequest(LoginData(email, pass))
        }else{
            view.hideProgressBar()
            EventBus.getDefault().post(LoginFailureEvent())
        }
    }

}