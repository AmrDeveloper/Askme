package com.amrdeveloper.askme

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.amrdeveloper.askme.data.User
import com.amrdeveloper.askme.extensions.notNull
import com.amrdeveloper.askme.net.AskmeClient
import com.amrdeveloper.askme.net.DEFAULT_QUERY_COUNT
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

class UserDataSource : PageKeyedDataSource<Int, User>(){

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>
    ) {
        AskmeClient.getUserService().getUsersQuery()
            .enqueue(object : Callback<List<User>>{
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    val userList = response.body()
                    userList.notNull {
                        val size = userList?.size
                        if(size == DEFAULT_QUERY_COUNT){
                            callback.onResult(userList, null, 1)
                        }else{
                            callback.onResult(userList!!, null, 0)
                        }
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Log.d("User", "Invalid Request")
                }
            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        AskmeClient.getUserService().getUsersQuery()
            .enqueue(object : Callback<List<User>>{
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    val userList = response.body()
                    userList.notNull {
                        if(params.key > 1){
                            callback.onResult(userList!!, params.key - 1)
                        }else{
                            callback.onResult(userList!!, null)
                        }
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Log.d("User", "Invalid Request")
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        AskmeClient.getUserService().getUsersQuery()
            .enqueue(object : Callback<List<User>>{
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    val userList = response.body()
                    userList.notNull {
                        val size = userList?.size
                        if(size == DEFAULT_QUERY_COUNT){
                            callback.onResult(userList, params.key + 1)
                        }else{
                            callback.onResult(userList!!, null)
                        }
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Log.d("User", "Invalid Request")
                }
            })
    }
}