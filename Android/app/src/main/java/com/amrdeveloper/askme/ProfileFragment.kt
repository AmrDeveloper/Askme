package com.amrdeveloper.askme

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.amrdeveloper.askme.data.User
import com.amrdeveloper.askme.extensions.notNull
import com.amrdeveloper.askme.extensions.setTextOrGone
import com.amrdeveloper.askme.net.AskmeClient
import com.amrdeveloper.askme.utils.Session
import kotlinx.android.synthetic.main.profile_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_layout, container, false)
        getUserInformation()
        return view
    }

    private fun getUserInformation(){
        val userEmail = Session().getUserEmail(context!!)
        AskmeClient.getUserService().getUserByEmail(userEmail.toString()).enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                response.body().notNull {
                    bindUserProfile(it)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(context, "Can't Load Info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun bindUserProfile(user : User){
        userName.setTextOrGone(user.username)
        userFollowing.text = "Following : ${user.followingNum}"
        userFollowers.text = "Followers : ${user.followersNum}"
        userLikes.text = "Likes : ${user.reactionsNum}"
        userQuestions.text = "Questions : ${user.questionsNum}"
        userAnswers.text = "Answers : ${user.answersNum}"
        userAddress.setTextOrGone(user.address)
        userStatus.setTextOrGone(user.status)
        userJoinDate.setTextOrGone(user.joinDate)
    }
}