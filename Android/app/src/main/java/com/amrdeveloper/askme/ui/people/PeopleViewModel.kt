package com.amrdeveloper.askme.ui.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.data.User
import com.amrdeveloper.askme.data.remote.datasource.UserDataSource
import com.amrdeveloper.askme.data.remote.datasource.UserSearchDataSource
import com.amrdeveloper.askme.data.remote.net.PagingConfig
import com.amrdeveloper.askme.data.remote.net.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(private val userService: UserService) : ViewModel(){

    private var usersLiveData : LiveData<PagedList<User>> = MutableLiveData()
    private var usersSearchLiveData : LiveData<PagedList<User>> = MutableLiveData()
    private lateinit var liveDataSource : LiveData<PageKeyedDataSource<Int, User>>
    private lateinit var searchLiveDataSource : LiveData<PageKeyedDataSource<Int, User>>

    fun loadPeopleList(){
        val userDataSourceFactory = UserDataSourceFactory()
        liveDataSource = userDataSourceFactory.getUserLiveDataSource()

        usersLiveData = LivePagedListBuilder(userDataSourceFactory, PagingConfig.getConfig()).build()
    }

    fun searchPeopleList(query : String){
        val userDataSourceFactory = UserSearchDataSourceFactory(query)
        searchLiveDataSource = userDataSourceFactory.getUserSearchLiveDataSource()

        usersSearchLiveData = LivePagedListBuilder(userDataSourceFactory, PagingConfig.getConfig()).build()
    }

    fun getUserPagedList() = usersLiveData
    fun getUsersSearchList() = usersSearchLiveData

    private inner class UserDataSourceFactory : DataSource.Factory<Int, User>(){

        private val userLiveDataSource : MutableLiveData<PageKeyedDataSource<Int, User>> = MutableLiveData()

        override fun create(): DataSource<Int, User> {
            val userDataSource = UserDataSource(viewModelScope, userService)
            userLiveDataSource.postValue(userDataSource)
            return userDataSource
        }

        fun getUserLiveDataSource() = userLiveDataSource
    }

    private inner class UserSearchDataSourceFactory(val query : String) : DataSource.Factory<Int, User>() {

        private val userLiveDataSource : MutableLiveData<PageKeyedDataSource<Int, User>> = MutableLiveData()

        override fun create(): DataSource<Int, User> {
            val userDataSource = UserSearchDataSource(query, viewModelScope, userService)
            userLiveDataSource.postValue(userDataSource)
            return userDataSource
        }

        fun getUserSearchLiveDataSource() = userLiveDataSource
    }
}