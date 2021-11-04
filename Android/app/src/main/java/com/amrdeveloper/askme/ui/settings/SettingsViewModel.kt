package com.amrdeveloper.askme.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.*
import com.amrdeveloper.askme.data.source.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _themeColor = MutableLiveData<ThemeColor>()
    val themeColor = _themeColor

    private val _messages = MutableLiveData<Int>()
    val messages : LiveData<Int> = _messages

    fun changeUserStatus(token : String, userId : String, status : String){
        viewModelScope.launch {
            val statusBody = StatusBody(userId, status)
            val result = userRepository.updateUserStatus(token, statusBody)
            if (result.isSuccess && result.getOrNull()?.code() == 200) {
                _messages.value = R.string.success_update_status
            } else {
                _messages.value = R.string.error_update_status
            }
        }
    }

    fun changeUserLocation(token : String, userId : String, location : String){
        viewModelScope.launch {
            val locationBody = LocationBody(userId, location)
            val result = userRepository.updateUserAddress(token, locationBody)
            if (result.isSuccess && result.getOrNull()?.code() == 200) {
                _messages.value = R.string.success_update_location
            } else {
                _messages.value = R.string.error_update_location
            }
        }
    }

    fun changeUserColor(token : String, userId : String, color : ThemeColor) {
        viewModelScope.launch {
            val colorBody = ColorBody(userId, color.name)
            val result = userRepository.updateUserColor(token, colorBody)
            if (result.isSuccess && result.getOrNull()?.code() == 200)  {
                themeColor.value = color
                _messages.value = R.string.success_update_color
            } else {
                _messages.value = R.string.error_update_color
            }
        }
    }

    fun changeUserPassword(token : String, userId : String, password : String) {
       viewModelScope.launch {
           val passwordBody = PasswordBody(userId, password)
           val result = userRepository.updateUserPassword(token, passwordBody)
           if (result.isSuccess && result.getOrNull()?.code() == 200) {
               _messages.value = R.string.success_update_password
           } else {
               _messages.value = R.string.error_update_password
           }
       }
    }
}