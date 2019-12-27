package com.amrdeveloper.askme.presenters

import com.amrdeveloper.askme.contracts.RegisterContract
import com.amrdeveloper.askme.models.RegisterData
import com.amrdeveloper.askme.events.RegisterFailureEvent
import com.amrdeveloper.askme.viewmodels.RegisterModel
import org.greenrobot.eventbus.EventBus

class RegisterPresenter(private val view : RegisterContract.View) : RegisterContract.Presenter{

    private val model: RegisterContract.Model

    init{
        model = RegisterModel()
    }

    override fun makeRegisterRequest(registerData: RegisterData) {
        val isValidInfo = model.isValidInformation(registerData)
        if(isValidInfo){
            view.showProgressBar()
            model.makeRegisterRequest(registerData)
        }else{
            view.hideProgressBar()
            EventBus.getDefault().post(RegisterFailureEvent("Invalid Register"))
        }
    }

}