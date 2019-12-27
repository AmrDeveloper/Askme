package com.amrdeveloper.askme.models

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.amrdeveloper.askme.data.User

class UserDataSourceFactory : DataSource.Factory<Int, User>(){

    private val userLiveDataSource : MutableLiveData<PageKeyedDataSource<Int, User>> = MutableLiveData()

    override fun create(): DataSource<Int, User> {
        val userDataSource = UserDataSource()
        userLiveDataSource.postValue(userDataSource)
        return userDataSource
    }

    fun getUserLiveDataSource() = userLiveDataSource
}