package com.amrdeveloper.askme.ui.settings

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.data.*
import com.amrdeveloper.askme.data.source.remote.net.ResponseData
import com.amrdeveloper.askme.data.source.remote.net.ResponseType
import com.amrdeveloper.askme.data.source.remote.net.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val userService: UserService) : ViewModel(){

    private val statusLiveData : MutableLiveData<ResponseType> = MutableLiveData()

    private val locationLiveData : MutableLiveData<ResponseType> = MutableLiveData()

    private val colorLiveData : MutableLiveData<ResponseData<ThemeColor>> = MutableLiveData()

    private val passwordLiveData : MutableLiveData<ResponseData<String>> = MutableLiveData()

    fun changeUserStatus(token : String, userId : String, status : String){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val statusBody =
                    StatusBody(userId, status)
                val response = userService.updateUserStatus(token, statusBody)
                when(response.code()){
                    200 -> statusLiveData.postValue(ResponseType.SUCCESS)
                    403 -> statusLiveData.postValue(ResponseType.NO_AUTH)
                    else -> statusLiveData.postValue(ResponseType.FAILURE)
                }
            }catch (exception : Exception){
                Log.d("UPDATE","Message ${exception.message}")
                statusLiveData.postValue(ResponseType.FAILURE)
            }
        }
    }

    fun changeUserLocation(token : String, userId : String, location : String){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val locationBody =
                    LocationBody(userId, location)
                val response = userService.updateUserAddress(token, locationBody)
                when(response.code()){
                    200 -> locationLiveData.postValue(ResponseType.SUCCESS)
                    403 -> locationLiveData.postValue(ResponseType.NO_AUTH)
                    else -> locationLiveData.postValue(ResponseType.FAILURE)
                }
            }catch (exception : Exception){
                locationLiveData.postValue(ResponseType.FAILURE)
            }
        }
    }

    fun changeUserColor(token : String, userId : String, color : ThemeColor){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val colorBody = ColorBody(userId, color.name)
                val response = userService.updateUserColor(token, colorBody)
                Log.d("UPDATE","Message ${response.message()}")
                when(response.code()){
                    200 -> colorLiveData.postValue(ResponseData(ResponseType.SUCCESS, color))
                    403 -> colorLiveData.postValue(ResponseData(ResponseType.NO_AUTH, ThemeColor.ORANGE))
                    else -> colorLiveData.postValue(ResponseData(ResponseType.FAILURE, ThemeColor.ORANGE))
                }
            }catch (exception : Exception){
                colorLiveData.postValue(ResponseData(ResponseType.FAILURE, ThemeColor.ORANGE))
            }
        }
    }

    fun changeUserPassword(token : String, userId : String, password : String){
       viewModelScope.launch(Dispatchers.IO){
           try{
               val passwordBody =
                   PasswordBody(userId, password)
               val response = userService.updateUserPassword(token, passwordBody)
               Log.d("UPDATE","Message ${response.message()}")
               when(response.code()){
                   200 -> passwordLiveData.postValue(ResponseData(ResponseType.SUCCESS, password))
                   403 -> passwordLiveData.postValue(ResponseData(ResponseType.NO_AUTH, ""))
                   else -> passwordLiveData.postValue(ResponseData(ResponseType.FAILURE, ""))
               }
           }catch (exception : Exception){
               passwordLiveData.postValue(ResponseData(ResponseType.FAILURE, ""))
           }
       }
    }

    fun getStatusLiveData() = statusLiveData

    fun getLocationLiveData() = locationLiveData

    fun getColorLiveData() = colorLiveData

    fun getPasswordLiveData() = passwordLiveData
}