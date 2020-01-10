package com.amrdeveloper.askme.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.models.ColorBody
import com.amrdeveloper.askme.models.LocationBody
import com.amrdeveloper.askme.models.PasswordBody
import com.amrdeveloper.askme.models.StatusBody
import com.amrdeveloper.askme.net.ResponseType
import com.amrdeveloper.askme.net.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val userService: UserService) : ViewModel(){

    private val statusLiveData : MutableLiveData<ResponseType> = MutableLiveData()

    private val locationLiveData : MutableLiveData<ResponseType> = MutableLiveData()

    private val colorLiveData : MutableLiveData<ResponseType> = MutableLiveData()

    private val passwordLiveData : MutableLiveData<ResponseType> = MutableLiveData()

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

    fun changeUserColor(token : String, userId : String, color : String){
        viewModelScope.launch(Dispatchers.IO){
            try{
                //TODO : will add color later
                val colorBody = ColorBody(userId)
                val response = userService.updateUserColor(token, colorBody)
                Log.d("UPDATE","Message ${response.message()}")
                when(response.code()){
                    200 -> passwordLiveData.postValue(ResponseType.SUCCESS)
                    403 -> passwordLiveData.postValue(ResponseType.NO_AUTH)
                    else -> passwordLiveData.postValue(ResponseType.FAILURE)
                }
            }catch (exception : Exception){
                passwordLiveData.postValue(ResponseType.FAILURE)
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
                   200 -> statusLiveData.postValue(ResponseType.SUCCESS)
                   403 -> statusLiveData.postValue(ResponseType.NO_AUTH)
                   else -> statusLiveData.postValue(ResponseType.FAILURE)
               }
           }catch (exception : Exception){
               statusLiveData.postValue(ResponseType.FAILURE)
           }
       }
    }

    fun getStatusLiveData() = statusLiveData

    fun getLocationLiveData() = locationLiveData

    fun getColorLiveData() = colorLiveData

    fun getPasswordLiveData() = passwordLiveData
}