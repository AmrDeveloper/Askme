package com.amrdeveloper.askme.ui.notification

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.amrdeveloper.askme.data.Notification
import com.amrdeveloper.askme.data.source.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private var notificationPagedList: MutableLiveData<PagingData<Notification>> = MutableLiveData()

    fun loadUserNotifications(userId : String, token : String){
        viewModelScope.launch {
            notificationRepository.getUserNotifications(token, userId).collect {
                notificationPagedList.value = it
            }
        }
    }

    fun makeNotificationReaded(id : String, token : String){
        viewModelScope.launch {
            val result = notificationRepository.makeNotificationOpened(token, id)
            if (result.isSuccess) {
                val response = result.getOrNull()
                when(response?.code()){
                    200 -> Log.d("NotificationViewModel", "Notification Request is done Now")
                    403 -> Log.d("NotificationViewModel", "No Auth")
                    else -> Log.d("NotificationViewModel", "Error")
                }
            } else {

            }
        }
    }

    fun getNotificationList() = notificationPagedList
}

