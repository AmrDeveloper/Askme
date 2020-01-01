package com.amrdeveloper.askme.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.models.RegisterData
import com.amrdeveloper.askme.net.AskmeClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel(){

    private val registerLiveData : MutableLiveData<String> = MutableLiveData()

    fun userRegister(registerData: RegisterData){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = AskmeClient.getUserService().register(registerData)
                when (response.code()) {
                    200 -> {
                        registerLiveData.postValue("valid")
                    }
                    400 -> {
                        registerLiveData.postValue("invalid")
                    }
                    else -> {
                        registerLiveData.postValue("invalid")
                    }
                }
            }catch (exception : Exception){
                Log.d("Login", "Invalid Request")
            }
        }
    }

    fun getRegisterLiveData() = registerLiveData
}