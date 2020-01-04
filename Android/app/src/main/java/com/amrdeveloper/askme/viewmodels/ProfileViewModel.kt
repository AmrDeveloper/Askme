package com.amrdeveloper.askme.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.amrdeveloper.askme.extensions.str
import com.amrdeveloper.askme.models.Feed
import com.amrdeveloper.askme.models.Follow
import com.amrdeveloper.askme.models.FollowData
import com.amrdeveloper.askme.models.User
import com.amrdeveloper.askme.net.AskmeClient
import com.amrdeveloper.askme.net.PagingConfig
import com.amrdeveloper.askme.net.ResponseType
import com.amrdeveloper.askme.utils.FileUtils
import com.amrdeveloper.askme.utils.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception

private const val TAG = "ProfileViewModel"

class ProfileViewModel : ViewModel() {

    private val userLiveData: MutableLiveData<User> = MutableLiveData()
    private val followLiveData : MutableLiveData<Follow> = MutableLiveData()
    private val avatarLiveData : MutableLiveData<ResponseType> = MutableLiveData()
    private val wallpaperLiveData : MutableLiveData<ResponseType> = MutableLiveData()
    private var feedPagedList: LiveData<PagedList<Feed>> = MutableLiveData()
    private lateinit var liveDataSource: LiveData<PageKeyedDataSource<Int, Feed>>

    fun loadUserFeed(userId : String){
        val feedDataSourceFactory = FeedDataSourceFactory(userId)
        liveDataSource = feedDataSourceFactory.getFeedLiveDataSource()

        feedPagedList = LivePagedListBuilder(feedDataSourceFactory, PagingConfig.getConfig()).build()
    }

    fun loadUserInformation(userId : String, localId : String){
        viewModelScope.launch(Dispatchers.IO){
            val user =  AskmeClient.getUserService().getUserById(userId, localId)
            userLiveData.postValue(user)
        }
    }

    fun updateUserAvatar(context: Context, avatar : Uri){
        val emailBody = RequestBody.create(MediaType.parse("multipart/form-data"), Session.getUserEmail(context).str())

        val imagePath = FileUtils.getImagePath(context, avatar)
        val imageFile = File(imagePath)
        val requestFile: RequestBody = RequestBody.create(MediaType.parse(FileUtils.getFileType(context, avatar)), imageFile)
        val avatarBody =  MultipartBody.Part.createFormData("avatar", imageFile.name, requestFile)

        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = AskmeClient.getUserService().updateUserAvatar(emailBody, avatarBody)
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
        val emailBody = RequestBody.create(MediaType.parse("multipart/form-data"), Session.getUserEmail(context).str())

        val imagePath = FileUtils.getImagePath(context, wallpaper)
        val imageFile = File(imagePath)
        val requestFile: RequestBody = RequestBody.create(MediaType.parse(FileUtils.getFileType(context, wallpaper)), imageFile)
        val wallpaperBody =  MultipartBody.Part.createFormData("wallpaper", imageFile.name, requestFile)

        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = AskmeClient.getUserService().updateUserWallpaper(emailBody, wallpaperBody)
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
                val response = AskmeClient.getFollowService().followUser("auth $token", followData)
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
                val response = AskmeClient.getFollowService().unFollowUser("auth $token", followData)
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

    private inner class FeedDataSourceFactory(var userId : String) : DataSource.Factory<Int,Feed>() {

        private val feedLiveDataSource : MutableLiveData<PageKeyedDataSource<Int, Feed>> = MutableLiveData()

        override fun create(): DataSource<Int, Feed> {
            val feedDataSource = FeedDataSource(userId, viewModelScope)
            feedLiveDataSource.postValue(feedDataSource)
            return feedDataSource
        }

        fun getFeedLiveDataSource() = feedLiveDataSource
    }
}