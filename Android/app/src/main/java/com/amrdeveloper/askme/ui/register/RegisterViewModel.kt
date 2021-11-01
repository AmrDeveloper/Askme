package com.amrdeveloper.askme.ui.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.data.RegisterData
import com.amrdeveloper.askme.data.SessionData
import com.amrdeveloper.askme.data.source.remote.net.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authService: AuthService): ViewModel(){

    private val registerLiveData : MutableLiveData<SessionData> = MutableLiveData()

    fun userRegister(registerData: RegisterData){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = authService.register(registerData)
                when (response.code()) {
                    200 -> {
                        val sessionData = response.body()
                        registerLiveData.postValue(sessionData)
                    }
                    else -> {
                        registerLiveData.postValue(null)
                    }
                }
            }catch (exception : Exception){
                Log.d("Login", "Invalid Request")
            }
        }
    }

    fun getRegisterLiveData() = registerLiveData
}