package com.amrdeveloper.askme.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.data.*
import com.amrdeveloper.askme.data.ResponseData
import com.amrdeveloper.askme.data.ResponseType
import com.amrdeveloper.askme.data.source.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val statusLiveData : MutableLiveData<ResponseType> = MutableLiveData()

    private val locationLiveData : MutableLiveData<ResponseType> = MutableLiveData()

    private val colorLiveData : MutableLiveData<ResponseData<ThemeColor>> = MutableLiveData()

    private val passwordLiveData : MutableLiveData<ResponseData<String>> = MutableLiveData()

    fun changeUserStatus(token : String, userId : String, status : String){
        viewModelScope.launch {
            val statusBody = StatusBody(userId, status)
            val result = userRepository.updateUserStatus(token, statusBody)
            if (result.isSuccess) {
                when (result.getOrNull()?.code()) {
                    200 -> statusLiveData.postValue(ResponseType.SUCCESS)
                    403 -> statusLiveData.postValue(ResponseType.NO_AUTH)
                    else -> statusLiveData.postValue(ResponseType.FAILURE)
                }
            } else {
                statusLiveData.postValue(ResponseType.FAILURE)
            }
        }
    }

    fun changeUserLocation(token : String, userId : String, location : String){
        viewModelScope.launch {
            val locationBody = LocationBody(userId, location)
            val result = userRepository.updateUserAddress(token, locationBody)
            if (result.isSuccess) {
                when (result.getOrNull()?.code()) {
                    200 -> locationLiveData.postValue(ResponseType.SUCCESS)
                    403 -> locationLiveData.postValue(ResponseType.NO_AUTH)
                    else -> locationLiveData.postValue(ResponseType.FAILURE)
                }
            } else {
                locationLiveData.postValue(ResponseType.FAILURE)
            }
        }
    }

    fun changeUserColor(token : String, userId : String, color : ThemeColor) {
        viewModelScope.launch {
            val colorBody = ColorBody(userId, color.name)
            val result = userRepository.updateUserColor(token, colorBody)
            if (result.isSuccess) {
                when(result.getOrNull()?.code()){
                    200 -> colorLiveData.postValue(ResponseData(ResponseType.SUCCESS, color))
                    403 -> colorLiveData.postValue(ResponseData(ResponseType.NO_AUTH, ThemeColor.ORANGE))
                    else -> colorLiveData.postValue(ResponseData(ResponseType.FAILURE, ThemeColor.ORANGE))
                }
            } else {

            }
        }
    }

    fun changeUserPassword(token : String, userId : String, password : String) {
       viewModelScope.launch {
           val passwordBody = PasswordBody(userId, password)
           val result = userRepository.updateUserPassword(token, passwordBody)
           if (result.isSuccess) {
               when (result.getOrNull()?.code()) {
                   200 -> passwordLiveData.postValue(ResponseData(ResponseType.SUCCESS, password))
                   403 -> passwordLiveData.postValue(ResponseData(ResponseType.NO_AUTH, ""))
                   else -> passwordLiveData.postValue(ResponseData(ResponseType.FAILURE, ""))
               }
           } else {

           }
       }
    }

    fun getStatusLiveData() = statusLiveData

    fun getLocationLiveData() = locationLiveData

    fun getColorLiveData() = colorLiveData

    fun getPasswordLiveData() = passwordLiveData
}