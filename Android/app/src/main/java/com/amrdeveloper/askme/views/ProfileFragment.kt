package com.amrdeveloper.askme.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.adapter.FeedPagedAdapter
import com.amrdeveloper.askme.contracts.ProfileContract
import com.amrdeveloper.askme.data.Feed
import com.amrdeveloper.askme.data.User
import com.amrdeveloper.askme.events.LoadFinishEvent
import com.amrdeveloper.askme.extensions.*
import com.amrdeveloper.askme.models.FeedViewModel
import com.amrdeveloper.askme.net.AskmeClient
import com.amrdeveloper.askme.presenters.ProfilePresenter
import com.amrdeveloper.askme.utils.AskmeFragment
import com.amrdeveloper.askme.utils.Session
import kotlinx.android.synthetic.main.list_layout.*
import kotlinx.android.synthetic.main.profile_layout.*
import kotlinx.android.synthetic.main.user_grid_analysis.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : AskmeFragment(), ProfileContract.View{

    private lateinit var mProfilePresenter: ProfilePresenter
    private lateinit var mFeedAdapter: FeedPagedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_layout, container, false)
        feedListSetup(view)
        getUserInformation()
        loadUserFeed()
        return view
    }

    private fun feedListSetup(view : View){
        mFeedAdapter = FeedPagedAdapter()
        val listItems = view.findViewById<RecyclerView>(R.id.listItems)
        listItems.setHasFixedSize(true)
        listItems.layoutManager = LinearLayoutManager(context)
        listItems.adapter = mFeedAdapter
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
        userAddress.setTextOrGone(user.address)
        userStatus.setTextOrGone(user.status)
        userJoinDate.setTextOrGone(user.joinDate)

        userFollowing.text = user.followingNum.toString()
        userFollowers.text = user.followersNum.toString()
        userLikes.text = user.reactionsNum.toString()
        userQuestions.text = user.questionsNum.toString()
        userAnswers.text = user.answersNum.toString()

        userAvatar.loadImage(user.avatarUrl)
        userWallpaper.loadImage(user.wallpaperUrl)
    }

    private fun loadUserFeed(){
        val userId  = Session().getUserId(context!!).toString()
        FeedViewModel.setUserId(userId)
        val feedViewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)

        mProfilePresenter = ProfilePresenter(this,feedViewModel,this)
        mProfilePresenter.startLoadingFeed()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoadFinishEvent(event : LoadFinishEvent<PagedList<Feed>>){
        mFeedAdapter.submitList(event.data)
        hideProgressBar()
    }

    override fun showProgressBar() {
        loadingBar.show()
    }

    override fun hideProgressBar() {
        loadingBar.gone()
    }
}