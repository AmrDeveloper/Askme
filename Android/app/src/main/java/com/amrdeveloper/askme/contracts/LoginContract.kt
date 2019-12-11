package com.amrdeveloper.askme.contracts

import com.amrdeveloper.askme.LoginData

interface LoginContract {

    interface Model{
        fun isValidInformation(email : String, pass : String) : Boolean
        fun makeLoginRequest(loginData: LoginData)
    }

    interface View{
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter{
        fun makeLoginRequest(email : String, pass : String)
    }
}