package com.amrdeveloper.askme.views

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.adapter.FeedAdapter
import com.amrdeveloper.askme.models.*
import com.amrdeveloper.askme.databinding.ProfileLayoutBinding
import com.amrdeveloper.askme.extensions.*
import com.amrdeveloper.askme.viewmodels.ProfileViewModel
import com.amrdeveloper.askme.net.AskmeClient
import com.amrdeveloper.askme.utils.Session
import kotlinx.android.synthetic.main.profile_layout.*
import kotlinx.android.synthetic.main.user_grid_analysis.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment(){

    private lateinit var mUserId: String
    private lateinit var mCurrentUser : User

    private lateinit var mFeedAdapter: FeedAdapter
    private lateinit var mProfileBinding: ProfileLayoutBinding
    private lateinit var mProfileViewModel : ProfileViewModel

    private val LOG_TAG = "ProfileFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mProfileBinding = DataBindingUtil.inflate(inflater, R.layout.profile_layout, container, false)
        mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        updateUserInfoFromArguments()
        setupFeedRecyclerView()

        mProfileBinding.listLayout.loadingBar.show()

        mProfileViewModel.loadUserInformation(mUserId, Session().getUserId(context!!).str())
        mProfileViewModel.loadUserFeed(mUserId)

        mProfileViewModel.getUserLiveData().observe(this, Observer {
            mCurrentUser = it
            bindUserProfile(it)
        })

        mProfileViewModel.getFeedPagedList().observe(this, Observer {
            mFeedAdapter.submitList(it)
            mProfileBinding.listLayout.loadingBar.gone()
        })

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

    private fun updateUserInfoFromArguments(){
        mUserId = arguments?.getString(Constants.USER_ID).str()
        if (mUserId.isNullString()) {
            mUserId = Session().getUserId(context!!).str()
        }
    }

    private fun setupFeedRecyclerView() {
        mFeedAdapter = FeedAdapter()
        mProfileBinding.listLayout.listItems.setHasFixedSize(true)
        mProfileBinding.listLayout.listItems.layoutManager = LinearLayoutManager(context)
        mProfileBinding.listLayout.listItems.isNestedScrollingEnabled = false
        mProfileBinding.listLayout.listItems.adapter = mFeedAdapter
    }

    private fun bindUserProfile(user: User) {
        userName.setTextOrGone(user.username)
        userAddress.setTextOrGone(user.address)
        userStatus.setTextOrGone(user.status)
        userJoinDate.setFormattedJoinDate(user.joinDate)

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
}