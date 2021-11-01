package com.amrdeveloper.askme.ui.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.paging.PagingConfig
import com.amrdeveloper.askme.data.*
import com.amrdeveloper.askme.data.source.FeedDataSource
import com.amrdeveloper.askme.data.source.remote.paging.FeedPagingDataSource
import com.amrdeveloper.askme.data.source.remote.service.*
import com.amrdeveloper.askme.utils.FileUtils
import com.amrdeveloper.askme.utils.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

private const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userService: UserService,
                                           private val followService: FollowService,
                                           private val feedDataSource: FeedDataSource,
                                           private val feedService: FeedService) : ViewModel() {

    private val userLiveData: MutableLiveData<User> = MutableLiveData()
    private val followLiveData : MutableLiveData<Follow> = MutableLiveData()
    private val avatarLiveData : MutableLiveData<ResponseType> = MutableLiveData()
    private val wallpaperLiveData : MutableLiveData<ResponseType> = MutableLiveData()
    private var feedPagedList: MutableLiveData<PagingData<Feed>> = MutableLiveData()

    fun loadUserFeed(id: String, userId : String){
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = 10, enablePlaceholders = false),
                pagingSourceFactory = { FeedPagingDataSource(feedDataSource, id, userId) }).flow.collect {
                feedPagedList.value = it
            }
        }
    }

    fun loadUserInformation(userId : String, localId : String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val user =  userService.getUserById(userId, localId)
                userLiveData.postValue(user)
            } catch (e : Exception) {
                Log.d(TAG, "Invalid Request")
            }
        }
    }

    fun updateUserAvatar(context: Context, avatar : Uri){
        val emailBody = RequestBody.create(MediaType.parse("multipart/form-data"), Session.getUserEmail(context).toString())

        val imagePath = FileUtils.getImagePath(context, avatar)
        val imageFile = File(imagePath)
        val requestFile: RequestBody = RequestBody.create(MediaType.parse(FileUtils.getFileType(context, avatar)), imageFile)
        val avatarBody =  MultipartBody.Part.createFormData("avatar", imageFile.name, requestFile)

        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = userService.updateUserAvatar(emailBody, avatarBody)
                when(response.code()){
                    200 -> avatarLiveData.postValue(ResponseType.SUCCESS)
                    401 -> avatarLiveData.postValue(ResponseType.NO_AUTH)
                    else -> avatarLiveData.postValue(ResponseType.FAILURE)
                }
            }catch(exception : Exception){
                avatarLiveData.postValue(ResponseType.FAILURE)
                Log.d(TAG, "Invalid Request")
            }
        }
    }

    fun updateUserWallpaper(context: Context, wallpaper : Uri){
        val emailBody = RequestBody.create(MediaType.parse("multipart/form-data"), Session.getUserEmail(context).toString())

        val imagePath = FileUtils.getImagePath(context, wallpaper)
        val imageFile = File(imagePath)
        val requestFile: RequestBody = RequestBody.create(MediaType.parse(FileUtils.getFileType(context, wallpaper)), imageFile)
        val wallpaperBody =  MultipartBody.Part.createFormData("wallpaper", imageFile.name, requestFile)

        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = userService.updateUserWallpaper(emailBody, wallpaperBody)
                when(response.code()){
                    200 -> wallpaperLiveData.postValue(ResponseType.SUCCESS)
                    401 -> wallpaperLiveData.postValue(ResponseType.NO_AUTH)
                    else -> wallpaperLiveData.postValue(ResponseType.FAILURE)
                }
            }catch(exception : Exception){
                wallpaperLiveData.postValue(ResponseType.FAILURE)
                Log.d(TAG, "Invalid Request")
            }
        }
    }

    fun followUser(token: String, followData: FollowData){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = followService.followUser("auth $token", followData)
                if (response.code() == 200) {
                    followLiveData.postValue(Follow.FOLLOW)
                }
            }catch (exception : Exception){
                Log.d(TAG, "Invalid Request")
            }
        }
    }

    fun unfollowUser(token: String, followData: FollowData){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = followService.unFollowUser("auth $token", followData)
                if (response.code() == 200) {
                    followLiveData.postValue(Follow.UN_FOLLOW)
                }
            }catch (exception : Exception){
                Log.d(TAG, "Invalid Request")
            }
        }
    }

    fun getUserLiveData() = userLiveData

    fun getFeedPagedList() = feedPagedList

    fun getFollowLiveData() = followLiveData

    fun getAvatarLiveData() = avatarLiveData

    fun getWallpaperLiveData() = wallpaperLiveData
}