package com.amrdeveloper.askme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.models.User

class UserViewModel : ViewModel(){

    private var usersLiveData : LiveData<PagedList<User>> = MutableLiveData()
    private var usersSearchLiveData : LiveData<PagedList<User>> = MutableLiveData()
    private lateinit var liveDataSource : LiveData<PageKeyedDataSource<Int, User>>
    private lateinit var searchLiveDataSource : LiveData<PageKeyedDataSource<Int, User>>

    fun loadPeopleList(){
        val userDataSourceFactory = UserDataSourceFactory()
        liveDataSource = userDataSourceFactory.getUserLiveDataSource()

        val config: PagedList.Config =
            PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .build()

        usersLiveData = LivePagedListBuilder(userDataSourceFactory, config).build()
    }

    fun searchPeopleList(query : String){
        val userDataSourceFactory = UserSearchDataSourceFactory(query)
        searchLiveDataSource = userDataSourceFactory.getUserSearchLiveDataSource()

        val config: PagedList.Config =
            PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .build()

        usersSearchLiveData = LivePagedListBuilder(userDataSourceFactory, config).build()
    }

    fun getUserPagedList() = usersLiveData
    fun getUsersSearchList() = usersSearchLiveData

    private inner class UserDataSourceFactory : DataSource.Factory<Int, User>(){

        private val userLiveDataSource : MutableLiveData<PageKeyedDataSource<Int, User>> = MutableLiveData()

        override fun create(): DataSource<Int, User> {
            val userDataSource = UserDataSource(viewModelScope)
            userLiveDataSource.postValue(userDataSource)
            return userDataSource
        }

        fun getUserLiveDataSource() = userLiveDataSource
    }

    private inner class UserSearchDataSourceFactory(val query : String) : DataSource.Factory<Int, User>() {

        private val userLiveDataSource : MutableLiveData<PageKeyedDataSource<Int, User>> = MutableLiveData()

        override fun create(): DataSource<Int, User> {
            val userDataSource = UserSearchDataSource(query, viewModelScope)
            userLiveDataSource.postValue(userDataSource)
            return userDataSource
        }

        fun getUserSearchLiveDataSource() = userLiveDataSource
    }
}