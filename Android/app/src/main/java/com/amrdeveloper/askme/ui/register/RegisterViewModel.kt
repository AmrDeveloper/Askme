package com.amrdeveloper.askme.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.data.RegisterData
import com.amrdeveloper.askme.data.SessionData
import com.amrdeveloper.askme.data.source.AuthDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authDataSource: AuthDataSource
) : ViewModel() {

    private val registerLiveData : MutableLiveData<SessionData> = MutableLiveData()

    fun userRegister(registerData: RegisterData){
        viewModelScope.launch {
            val result = authDataSource.register(registerData)
            if (result.isSuccess) {
                val response = result.getOrNull()
                when(response?.code()) {
                    200 -> {
                        val sessionData = response.body()
                        registerLiveData.postValue(sessionData)
                    }
                    else -> registerLiveData.postValue(null)
                }
            }
            else {
                registerLiveData.postValue(null)
            }
        }
    }

    fun getRegisterLiveData() = registerLiveData
}