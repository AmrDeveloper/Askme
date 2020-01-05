package com.amrdeveloper.askme.viewmodels

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.amrdeveloper.askme.models.User
import com.amrdeveloper.askme.net.DEFAULT_QUERY_COUNT
import com.amrdeveloper.askme.net.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserDataSource(private val scope: CoroutineScope,
                     private val userService: UserService) :
    PageKeyedDataSource<Int, User>(){

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>
    ) {
        scope.launch(Dispatchers.IO){
            try{
                val users = userService.getUsersQuery()
                if(users.size == DEFAULT_QUERY_COUNT){
                    callback.onResult(users, null, 1)
                }else{
                    callback.onResult(users, null, 0)
                }
            }catch (exception : Exception){
                Log.d("User", "Invalid Request")
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        scope.launch(Dispatchers.IO){
            try{
                val users = userService.getUsersQuery(offset = params.key)
                if(params.key > 1){
                    callback.onResult(users, params.key - 1)
                }else{
                    callback.onResult(users, null)
                }
            }catch (exception : Exception){
                Log.d("User", "Invalid Request")
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        scope.launch(Dispatchers.IO){
            try{
                val users = userService.getUsersQuery(offset = params.key)
                if(users.size == DEFAULT_QUERY_COUNT){
                    callback.onResult(users, params.key + 1)
                }else{
                    callback.onResult(users, null)
                }
            }catch (exception : Exception){
                Log.d("User", "Invalid Request")
            }
        }
    }
}