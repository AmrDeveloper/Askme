package com.amrdeveloper.askme.views

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.adapter.FeedAdapter
import com.amrdeveloper.askme.contracts.ProfileContract
import com.amrdeveloper.askme.data.*
import com.amrdeveloper.askme.databinding.ProfileLayoutBinding
import com.amrdeveloper.askme.events.LoadFinishEvent
import com.amrdeveloper.askme.extensions.*
import com.amrdeveloper.askme.models.FeedViewModel
import com.amrdeveloper.askme.net.AskmeClient
import com.amrdeveloper.askme.presenters.ProfilePresenter
import com.amrdeveloper.askme.utils.Session
import kotlinx.android.synthetic.main.profile_layout.*
import kotlinx.android.synthetic.main.user_grid_analysis.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment(), ProfileContract.View {

    private lateinit var mUserId: String
    private lateinit var mCurrentUser : User

    private lateinit var mFeedAdapter: FeedAdapter
    private lateinit var mProfileBinding: ProfileLayoutBinding

    private val LOG_TAG = "ProfileFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.profile_layout, container, false)

        mUserId = arguments?.getString(Constants.USER_ID).str()
        if (mUserId.isNullString()) {
            mUserId = Session().getUserId(context!!).str()
        }

        feedListSetup()
        getUserInformation()
        openAskQuestionFragment()

        mProfileBinding.followCardView.setOnClickListener {
            val followData = FollowData(Session().getUserId(context!!).str(), mUserId)

            when (Follow.valueOf(mProfileBinding.followCardView.tag.toString())) {
                Follow.FOLLOW -> {
                    AskmeClient.getFollowService().unFollowUser(
                        token = "auth ${Session().getUserToken(context!!).str()}",
                        followData = followData
                    ).enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (response.code() == 200) {
                                updateFollowCardView(Follow.UN_FOLLOW)
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(context, "Can't unFollow user", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
                }
                Follow.UN_FOLLOW -> {
                    AskmeClient.getFollowService().followUser(
                        token = "auth ${Session().getUserToken(context!!).str()}",
                        followData = followData
                    ).enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (response.code() == 200) {
                                updateFollowCardView(Follow.FOLLOW)
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(context, "Can't Follow user", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
                }
            }
        }

        return mProfileBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logoutMenu){
            Session().logout(context!!)

            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun feedListSetup() {
        mFeedAdapter = FeedAdapter()
        mProfileBinding.listLayout.listItems.setHasFixedSize(true)
        mProfileBinding.listLayout.listItems.layoutManager = LinearLayoutManager(context)
        mProfileBinding.listLayout.listItems.isNestedScrollingEnabled = false
        mProfileBinding.listLayout.listItems.adapter = mFeedAdapter
    }

    private fun openAskQuestionFragment() {
        mProfileBinding.askmeButton.setOnClickListener {
            val askQuestionFragment = AskQuestionFragment()

            val args = Bundle()
            args.putString(Constants.USER_ID, mCurrentUser.id)
            args.putString(Constants.NAME, mCurrentUser.name)
            args.putString(Constants.USERNAME, mCurrentUser.username)
            args.putString(Constants.AVATAR_URL, mCurrentUser.avatarUrl)
            askQuestionFragment.arguments = args

            fragmentManager?.openFragmentInto(R.id.viewContainers,askQuestionFragment)
        }
    }

    private fun getUserInformation() {
        AskmeClient.getUserService().getUserByEmail(mUserId, Session().getUserId(context!!).str())
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    response.body().notNull {
                        mCurrentUser = it
                        bindUserProfile(it)
                        loadUserFeed(it.id)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(context, "Can't Load Info", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun bindUserProfile(user: User) {
        userName.setTextOrGone(user.username)
        userAddress.setTextOrGone(user.address)
        userStatus.setTextOrGone(user.status)
        userJoinDate.setTextOrGone(user.joinDate)

        userFollowing.text = user.followingNum.str()
        userFollowers.text = user.followersNum.str()
        userLikes.text = user.reactionsNum.str()
        userQuestions.text = user.questionsNum.str()
        userAnswers.text = user.answersNum.str()

        userAvatar.loadImage(user.avatarUrl)
        userWallpaper.loadImage(user.wallpaperUrl)

        if (user.id != Session().getUserId(context!!)) {
            updateFollowCardView(user.isUserFollow)
        } else {
            mProfileBinding.followCardView.gone()
        }
    }

    private fun updateFollowCardView(follow: Follow) {
        when (follow) {
            Follow.FOLLOW -> {
                mProfileBinding.followingTxt.text = getString(R.string.following)
                mProfileBinding.followIcon.setImageResource(R.drawable.ic_done_all)
                mProfileBinding.followCardView.tag = Follow.FOLLOW
            }
            Follow.UN_FOLLOW -> {
                mProfileBinding.followingTxt.text = getString(R.string.follow)
                mProfileBinding.followIcon.setImageResource(R.drawable.ic_feed)
                mProfileBinding.followCardView.tag = Follow.UN_FOLLOW
            }
        }
    }

    private fun loadUserFeed(userId: String) {
        FeedViewModel.setUserId(userId)
        val feedViewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        feedViewModel.getFeedPagedList().observe(this, Observer {
            mFeedAdapter.submitList(it)
            hideProgressBar()
        })

        //TODO : Move to MVVM
        //mProfilePresenter = ProfilePresenter(this, feedViewModel, this)
        //mProfilePresenter.startLoadingFeed()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoadFinishEvent(event: LoadFinishEvent<PagedList<Feed>>) {
        //mFeedAdapter.submitList(event.data)
        //hideProgressBar()
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