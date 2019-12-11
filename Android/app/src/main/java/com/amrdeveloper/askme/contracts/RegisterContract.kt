package com.amrdeveloper.askme.contracts

import com.amrdeveloper.askme.data.RegisterData

interface RegisterContract {

    interface Model {
        fun isValidInformation(registerData: RegisterData) : Boolean
        fun makeRegisterRequest(registerData: RegisterData)
    }

    interface View {
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter {
        fun makeRegisterRequest(registerData: RegisterData)
    }
}