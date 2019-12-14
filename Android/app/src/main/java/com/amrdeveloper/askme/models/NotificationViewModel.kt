package com.amrdeveloper.askme.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.data.Notification

class NotificationViewModel : ViewModel() {

    companion object{

        private lateinit var userId: String
        private lateinit var authToken: String

        fun setUserId(id: String) {
            userId = id
        }

        fun setToken(token : String){
            authToken = token
        }
    }

    private var notiPagedList: LiveData<PagedList<Notification>>
    private var liveDataSource: LiveData<PageKeyedDataSource<Int, Notification>>

    init {
        val notiDataSourceFactory =
            NotificationDataSourceFactory(userId, authToken)
        liveDataSource = notiDataSourceFactory.getNotificationLiveDataSource()

        val config: PagedList.Config =
            PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .build()

        notiPagedList = LivePagedListBuilder(notiDataSourceFactory, config).build()
    }

    fun getNotiPagedList() = notiPagedList
    fun getLiveDataSource() = liveDataSource
}