package com.amrdeveloper.askme.contracts

import com.amrdeveloper.askme.data.LoginData

interface LoginContract {

    interface Model{
        fun isValidEmail(email : String) : Boolean
        fun isValidPassword(password : String) : Boolean
        fun makeLoginRequest(loginData: LoginData)
    }

    interface View{
        fun showEmailErrorMessage()
        fun showPasswordErrorMessage()
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter{
        fun makeLoginRequest(loginData: LoginData)
    }
}