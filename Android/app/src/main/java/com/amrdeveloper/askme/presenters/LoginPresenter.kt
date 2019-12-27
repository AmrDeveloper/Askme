package com.amrdeveloper.askme.presenters

import com.amrdeveloper.askme.models.LoginData
import com.amrdeveloper.askme.contracts.LoginContract
import com.amrdeveloper.askme.viewmodels.LoginModel

class LoginPresenter(
    private val view : LoginContract.View
) : LoginContract.Presenter {

    private val model: LoginContract.Model

    init {
        model = LoginModel()
    }

    override fun makeLoginRequest(loginData: LoginData) {
        val isValidEmail = model.isValidEmail(loginData.email)
        val isValidPassword = model.isValidPassword(loginData.password)
        val isValidInfo : Boolean = isValidEmail and isValidPassword
        if(isValidInfo){
            view.showProgressBar()
            model.makeLoginRequest(loginData)
        }else{
            if(isValidEmail.not())view.showEmailErrorMessage()
            if(isValidPassword.not()) view.showPasswordErrorMessage()
        }
    }

}