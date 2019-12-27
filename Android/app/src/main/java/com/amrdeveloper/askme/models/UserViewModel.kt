package com.amrdeveloper.askme.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.data.User

class UserViewModel : ViewModel(){

    private var userPagedList : LiveData<PagedList<User>> = MutableLiveData()
    private lateinit var liveDataSource : LiveData<PageKeyedDataSource<Int, User>>

    fun loadPeopleList(){
        val userDataSourceFactory = UserDataSourceFactory()
        liveDataSource = userDataSourceFactory.getUserLiveDataSource()

        val config: PagedList.Config =
            PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .build()

        userPagedList = LivePagedListBuilder(userDataSourceFactory, config).build()
    }

    fun getUserPagedList() = userPagedList

    private inner class UserDataSourceFactory : DataSource.Factory<Int, User>(){

        private val userLiveDataSource : MutableLiveData<PageKeyedDataSource<Int, User>> = MutableLiveData()

        override fun create(): DataSource<Int, User> {
            val userDataSource = UserDataSource()
            userLiveDataSource.postValue(userDataSource)
            return userDataSource
        }

        fun getUserLiveDataSource() = userLiveDataSource
    }
}