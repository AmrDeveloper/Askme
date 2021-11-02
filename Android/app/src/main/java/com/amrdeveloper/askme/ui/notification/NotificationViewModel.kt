package com.amrdeveloper.askme.ui.notification

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.amrdeveloper.askme.data.Notification
import com.amrdeveloper.askme.data.source.NotificationDataSource
import com.amrdeveloper.askme.data.source.remote.paging.NotificationPagingDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationDataSource: NotificationDataSource
) : ViewModel() {

    private var notificationPagedList: MutableLiveData<PagingData<Notification>> = MutableLiveData()

    fun loadUserNotifications(userId : String, token : String){
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = 10, enablePlaceholders = false),
                pagingSourceFactory = { NotificationPagingDataSource(notificationDataSource, token, userId) }).flow.collect {
                notificationPagedList.value = it
            }
        }
    }

    fun makeNotificationReaded(id : String, token : String){
        viewModelScope.launch {
            val result = notificationDataSource.makeNotificationOpened(token, id)
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

