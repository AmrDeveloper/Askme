package com.amrdeveloper.askme.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.data.LoginData
import com.amrdeveloper.askme.data.SessionData
import com.amrdeveloper.askme.data.source.remote.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authService: AuthService) : ViewModel(){

    private val sessionLiveData : MutableLiveData<SessionData> = MutableLiveData()

    fun userLogin(loginData: LoginData){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = authService.login(loginData)
                if (response.code() == 200) {
                    val sessionData = response.body()
                    sessionLiveData.postValue(sessionData)
                } else {
                    sessionLiveData.postValue(null)
                }
            }catch (exception : Exception){
                Log.d("Login", "Invalid Request")
            }
        }
    }

    fun getSessionLiveData() = sessionLiveData
}