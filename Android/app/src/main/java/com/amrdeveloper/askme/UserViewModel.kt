package com.amrdeveloper.askme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.data.User

class UserViewModel : ViewModel(){

    private val userPagedList : LiveData<PagedList<User>>
    private val liveDataSource : LiveData<PageKeyedDataSource<Int, User>>

    init{
        val userDataSourceFactory = UserDataSourceFactory()
        liveDataSource = userDataSourceFactory.getUserLiveDataSource()

        val config: PagedList.Config =
            PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .build()

        userPagedList = LivePagedListBuilder(userDataSourceFactory, config).build()
    }

    fun getUserPagedList() = userPagedList
    fun getLiveDataSource() = liveDataSource
}