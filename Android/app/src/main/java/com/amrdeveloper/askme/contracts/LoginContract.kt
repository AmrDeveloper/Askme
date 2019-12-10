package com.amrdeveloper.askme.contracts

interface LoginContract {

    interface Model{
        fun isValidInformation(email : String, pass : String) : Boolean
        fun makeLoginRequest(email : String, pass : String)
    }

    interface View{
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter{
        fun onStartLogin(email : String, pass : String)
    }
}