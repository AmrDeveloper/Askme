package com.amrdeveloper.askme.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.adapter.FeedAdapter
import com.amrdeveloper.askme.contracts.ProfileContract
import com.amrdeveloper.askme.data.Feed
import com.amrdeveloper.askme.data.User
import com.amrdeveloper.askme.databinding.ProfileLayoutBinding
import com.amrdeveloper.askme.events.LoadFinishEvent
import com.amrdeveloper.askme.extensions.*
import com.amrdeveloper.askme.models.FeedViewModel
import com.amrdeveloper.askme.net.AskmeClient
import com.amrdeveloper.askme.presenters.ProfilePresenter
import com.amrdeveloper.askme.utils.Session
import kotlinx.android.synthetic.main.ask_question_layout.*
import kotlinx.android.synthetic.main.ask_question_layout.view.*
import kotlinx.android.synthetic.main.list_layout.view.*
import kotlinx.android.synthetic.main.profile_layout.*
import kotlinx.android.synthetic.main.user_grid_analysis.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment(), ProfileContract.View{

    private lateinit var mProfilePresenter: ProfilePresenter
    private lateinit var mFeedAdapter: FeedAdapter
    private lateinit var mProfileBinding : ProfileLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mProfileBinding = DataBindingUtil.inflate(inflater, R.layout.profile_layout, container,false)

        feedListSetup()
        getUserInformation()
        loadUserFeed()
        setupAskNewQuestion()
        return  mProfileBinding.root
    }

    private fun feedListSetup(){
        mFeedAdapter = FeedAdapter()
        mProfileBinding.listLayout.listItems.setHasFixedSize(true)
        mProfileBinding.listLayout.listItems.layoutManager = LinearLayoutManager(context)
        mProfileBinding.listLayout.listItems.isNestedScrollingEnabled = false
        mProfileBinding.listLayout.listItems.adapter = mFeedAdapter
    }

    private fun setupAskNewQuestion(){
        mProfileBinding.questionLayout.askButton.setOnClickListener {
            val fromUserId = Session().getUserId(context!!).toString()
            val isAnonymously = mProfileBinding.questionLayout.anonymouslySwitch.isChecked
            val questionBody = mProfileBinding.questionLayout.questionText.text?.trim()
            val toUserId = askText.getTag(0)
            //TODO : validate information
            //TODO : Make new question request
        }
    }

    private fun getUserInformation(){
        val userEmail = Session().getUserEmail(context!!)
        AskmeClient.getUserService().getUserByEmail(userEmail.toString()).enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                response.body().notNull {
                    bindUserProfile(it)
                    setupAskBordTitle(it)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(context, "Can't Load Info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupAskBordTitle(user : User){
        val localId = Session().getUserId(context!!)
        if(localId == user.id){
            askText.text = getString(R.string.ask_yourself)
        }else{
            askText.text = "Ask ${user.username}"
        }
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
        mProfileBinding.listLayout.loadingBar.show()
    }

    override fun hideProgressBar() {
        mProfileBinding.listLayout.loadingBar.gone()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}