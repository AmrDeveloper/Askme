package com.amrdeveloper.askme.contracts

import com.amrdeveloper.askme.data.LoginData

interface LoginContract {

    interface Model{
        fun isValidInformation(loginData: LoginData) : Boolean
        fun makeLoginRequest(loginData: LoginData)
    }

    interface View{
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter{
        fun makeLoginRequest(loginData: LoginData)
    }
}