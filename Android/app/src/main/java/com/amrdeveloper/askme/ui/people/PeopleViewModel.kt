package com.amrdeveloper.askme.ui.people

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.amrdeveloper.askme.data.User
import com.amrdeveloper.askme.data.source.UserDataSource
import com.amrdeveloper.askme.data.source.remote.paging.UserPagingDataSource
import com.amrdeveloper.askme.data.source.remote.paging.UserPagingSearchDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(private val userDataSource: UserDataSource) : ViewModel(){

    private var usersLiveData : MutableLiveData<PagingData<User>> = MutableLiveData()
    private var usersSearchLiveData : MutableLiveData<PagingData<User>> = MutableLiveData()

    fun loadPeopleList(){
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = 10, enablePlaceholders = false),
                pagingSourceFactory = { UserPagingDataSource(userDataSource) }).flow.collect {
                usersLiveData.value = it
            }
        }
    }

    fun searchPeopleList(query : String){
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = 10, enablePlaceholders = false),
                pagingSourceFactory = { UserPagingSearchDataSource(query, userDataSource) }).flow.collect {
                usersSearchLiveData.value = it
            }
        }
    }

    fun getUserPagedList() = usersLiveData
    fun getUsersSearchList() = usersSearchLiveData
}