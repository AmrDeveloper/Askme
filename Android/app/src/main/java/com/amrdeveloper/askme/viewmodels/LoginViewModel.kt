package com.amrdeveloper.askme.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.models.LoginData
import com.amrdeveloper.askme.models.SessionData
import com.amrdeveloper.askme.net.AuthService
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