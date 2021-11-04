package com.amrdeveloper.askme.ui.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.*
import com.amrdeveloper.askme.data.source.*
import com.amrdeveloper.askme.utils.FileUtils
import com.amrdeveloper.askme.utils.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor (
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository,
    private val feedRepository: FeedRepository,
) : ViewModel() {

    private val userLiveData: MutableLiveData<User> = MutableLiveData()
    private val followLiveData : MutableLiveData<Follow> = MutableLiveData()
    private var feedPagedList: MutableLiveData<PagingData<Feed>> = MutableLiveData()

    private val _messages = MutableLiveData<Int>()
    val messages : LiveData<Int> = _messages

    fun loadUserFeed(id: String, userId : String){
        viewModelScope.launch {
            feedRepository.getUserFeed(id, userId).collect {
                feedPagedList.value = it
            }
        }
    }

    fun loadUserInformation(userId : String, localId : String){
        viewModelScope.launch {
            val result =  userRepository.getUserById(userId, localId)
            if (result.isSuccess) {
                userLiveData.postValue(result.getOrNull())
            } else {
                _messages.value = R.string.error_load_user_info
            }
        }
    }

    fun updateUserAvatar(context: Context, avatar : Uri){
        val emailBody = RequestBody.create(MediaType.parse("multipart/form-data"), Session.getUserEmail(context).toString())

        val imagePath = FileUtils.getImagePath(context, avatar)
        val imageFile = File(imagePath)
        val requestFile: RequestBody = RequestBody.create(MediaType.parse(FileUtils.getFileType(context, avatar)), imageFile)
        val avatarBody =  MultipartBody.Part.createFormData("avatar", imageFile.name, requestFile)

        viewModelScope.launch {
            val result = userRepository.updateUserAvatar(emailBody, avatarBody)
            if (result.isSuccess && result.getOrNull()?.code() == 200) {
                _messages.value = R.string.success_update_avatar
            } else {
                _messages.value = R.string.error_update_avatar
            }
        }
    }

    fun updateUserWallpaper(context: Context, wallpaper : Uri){
        val emailBody = RequestBody.create(MediaType.parse("multipart/form-data"), Session.getUserEmail(context).toString())

        val imagePath = FileUtils.getImagePath(context, wallpaper)
        val imageFile = File(imagePath)
        val requestFile: RequestBody = RequestBody.create(MediaType.parse(FileUtils.getFileType(context, wallpaper)), imageFile)
        val wallpaperBody =  MultipartBody.Part.createFormData("wallpaper", imageFile.name, requestFile)

        viewModelScope.launch{
            val result = userRepository.updateUserWallpaper(emailBody, wallpaperBody)
            if (result.isSuccess && result.getOrNull()?.code() == 200) {
                _messages.value = R.string.success_update_wallpaper
            } else {
                _messages.value = R.string.error_update_wallpaper
            }
        }
    }

    fun followUser(token: String, followData: FollowData){
        viewModelScope.launch{
            val result = followRepository.followUser("auth $token", followData)
            if (result.isSuccess && result.getOrNull()?.code() == 200) {
                followLiveData.postValue(Follow.FOLLOW)
            } else {
                _messages.value = R.string.error_follow
            }
        }
    }

    fun unfollowUser(token: String, followData: FollowData){
        viewModelScope.launch {
            val result = followRepository.unFollowUser("auth $token", followData)
            if (result.isSuccess && result.getOrNull()?.code() == 200) {
                followLiveData.postValue(Follow.UN_FOLLOW)
            } else {
                _messages.value = R.string.error_unfollow
            }
        }
    }

    fun getUserLiveData() = userLiveData

    fun getFeedPagedList() = feedPagedList

    fun getFollowLiveData() = followLiveData
}