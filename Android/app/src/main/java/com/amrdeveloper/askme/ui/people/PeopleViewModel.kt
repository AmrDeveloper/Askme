package com.amrdeveloper.askme.ui.people

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.amrdeveloper.askme.data.User
import com.amrdeveloper.askme.data.source.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var usersLiveData : MutableLiveData<PagingData<User>> = MutableLiveData()
    private var usersSearchLiveData : MutableLiveData<PagingData<User>> = MutableLiveData()

    fun loadPeopleList(){
        viewModelScope.launch {
            userRepository.getUserList().collect {
                usersLiveData.value = it
            }
        }
    }

    fun searchPeopleList(query : String){
        viewModelScope.launch {
            userRepository.getUsersSearch(query).collect {
                usersSearchLiveData.value = it
            }
        }
    }

    fun getUserPagedList() = usersLiveData
    fun getUsersSearchList() = usersSearchLiveData
}