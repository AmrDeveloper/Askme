package com.amrdeveloper.askme.views

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.adapter.FeedAdapter
import com.amrdeveloper.askme.databinding.ProfileLayoutBinding
import com.amrdeveloper.askme.extensions.*
import com.amrdeveloper.askme.models.Constants
import com.amrdeveloper.askme.models.Follow
import com.amrdeveloper.askme.models.FollowData
import com.amrdeveloper.askme.models.User
import com.amrdeveloper.askme.net.ResponseType
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.profile_layout.*
import kotlinx.android.synthetic.main.user_grid_analysis.*

@AndroidEntryPoint
class ProfileFragment : Fragment(){

    private lateinit var mUserId: String
    private lateinit var mCurrentUser : User

    lateinit var mFeedAdapter: FeedAdapter
    private lateinit var mProfileBinding: ProfileLayoutBinding

    private val mProfileViewModel by viewModels<ProfileViewModel>()

    private val REQUEST_AVATAR_ID = 1996
    private val REQUEST_WALLPAPER_ID = 1997
    private val PERMISSION_EXTERNAL_STORAGE = 1998

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mProfileBinding = DataBindingUtil.inflate(inflater, R.layout.profile_layout, container, false)

        updateUserInfoFromArguments()
        setupFeedRecyclerView()

        mProfileBinding.listLayout.loadingBar.show()

        mProfileViewModel.loadUserInformation(mUserId, Session.getUserId(context!!).str())
        mProfileViewModel.loadUserFeed(mUserId, Session.getUserId(context!!).str())

        mProfileViewModel.getUserLiveData().observe(this, Observer {
            mCurrentUser = it
            bindUserProfile(it)
        })

        mProfileViewModel.getFeedPagedList().observe(this, Observer {
            mFeedAdapter.submitList(it)
            mProfileBinding.listLayout.loadingBar.gone()
        })

        mProfileViewModel.getAvatarLiveData().observe(this, Observer {
            when(it){
                ResponseType.SUCCESS -> {
                    Toast.makeText(context, "Update Avatar Success", Toast.LENGTH_SHORT).show()
                }
                ResponseType.FAILURE -> {
                    Toast.makeText(context, "Update Avatar Failure", Toast.LENGTH_SHORT).show()
                }
                ResponseType.NO_AUTH -> {
                    Toast.makeText(context, "Invalid Auth", Toast.LENGTH_SHORT).show()
                }
            }
        })

        mProfileViewModel.getWallpaperLiveData().observe(this, Observer {
            when(it){
                ResponseType.SUCCESS -> {
                    Toast.makeText(context, "Update Wallpaper Success", Toast.LENGTH_SHORT).show()
                }
                ResponseType.FAILURE -> {
                    Toast.makeText(context, "Update Wallpaper Failure", Toast.LENGTH_SHORT).show()
                }
                ResponseType.NO_AUTH -> {
                    Toast.makeText(context, "Invalid Auth", Toast.LENGTH_SHORT).show()
                }
            }
        })

        mProfileViewModel.getFollowLiveData().observe(this, Observer {updateFollowCardView(it)})

        mProfileBinding.askmeButton.setOnClickListener {openAskQuestionFragment()}

        mProfileBinding.followCardView.setOnClickListener {followCardViewListener()}

        setupFullScreenOption()
        return mProfileBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logoutMenu -> {
                Session.logout(context!!)
                fragmentManager?.openFragmentInto(R.id.viewContainers, LoginFragment())
            }
            R.id.settingsMenu -> {
                fragmentManager?.openFragmentInto(R.id.viewContainers, SettingsFragment())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateUserInfoFromArguments(){
        mUserId = arguments?.getString(Constants.USER_ID).str()
        if (mUserId.isNullString()) {
            mUserId = Session.getUserId(context!!).str()
            setupEditMode()
        }else{
            hideEditMode()
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

        userAvatar.loadImage(user.avatarUrl, R.drawable.ic_profile)
        userWallpaper.loadImage(user.wallpaperUrl, R.drawable.ic_wallpaper)

        if (user.id != Session.getUserId(context!!)) {
            updateFollowCardView(user.isUserFollow)
        } else {
            mProfileBinding.followCardView.gone()
        }
    }

    private fun setupFullScreenOption(){
        mProfileBinding.userAvatar.setOnClickListener {
            val fullscreenFragment = FullscreenFragment()

            val avatarUrl = mCurrentUser.avatarUrl
            val args = Bundle()
            args.putString(Constants.AVATAR_URL, avatarUrl)
            fullscreenFragment.arguments = args

            fragmentManager?.openFragmentInto(R.id.viewContainers, fullscreenFragment)
        }

        mProfileBinding.userWallpaper.setOnClickListener {
            val fullscreenFragment = FullscreenFragment()

            val avatarUrl = mCurrentUser.wallpaperUrl
            val args = Bundle()
            args.putString(Constants.AVATAR_URL, avatarUrl)
            fullscreenFragment.arguments = args

            fragmentManager?.openFragmentInto(R.id.viewContainers, fullscreenFragment)
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

    private fun setupEditMode(){
        mProfileBinding.changeAvatarAction.setOnClickListener { changeAvatarImage() }
        mProfileBinding.changeWallpaperAction.setOnClickListener { changeWallpaperImage() }
    }

    private fun hideEditMode(){
        mProfileBinding.changeAvatarAction.gone()
        mProfileBinding.changeWallpaperAction.gone()
        mProfileViewModel.getFollowLiveData().removeObservers(this)
        mProfileViewModel.getAvatarLiveData().removeObservers(this)
        mProfileViewModel.getWallpaperLiveData().removeObservers(this)
    }

    private fun openAskQuestionFragment(){
        val askQuestionFragment = AskQuestionFragment()

        val args = Bundle()
        args.putString(Constants.USER_ID, mCurrentUser.id)
        args.putString(Constants.NAME, mCurrentUser.name)
        args.putString(Constants.USERNAME, mCurrentUser.username)
        args.putString(Constants.AVATAR_URL, mCurrentUser.avatarUrl)
        askQuestionFragment.arguments = args

        fragmentManager?.openFragmentInto(R.id.viewContainers, askQuestionFragment)
    }

    private fun followCardViewListener(){
        val followData = FollowData(Session.getUserId(context!!).str(), mUserId)
        val token = Session.getUserToken(context!!).str()

        when (Follow.valueOf(mProfileBinding.followCardView.tag.toString())) {
            Follow.FOLLOW -> mProfileViewModel.unfollowUser(token ,followData)
            Follow.UN_FOLLOW -> mProfileViewModel.followUser(token ,followData)
        }
    }

    private fun changeAvatarImage(){
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, REQUEST_AVATAR_ID)
        }else{
            ActivityCompat.requestPermissions(activity!!,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_EXTERNAL_STORAGE)
        }
    }

    private fun changeWallpaperImage(){
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, REQUEST_WALLPAPER_ID)
        }else{
            ActivityCompat.requestPermissions(activity!!,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_EXTERNAL_STORAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
           when(requestCode){
               REQUEST_AVATAR_ID -> {
                   val avatarUri = data?.data
                   mProfileViewModel.updateUserAvatar(context!!, avatarUri!!)
               }
               REQUEST_WALLPAPER_ID -> {
                   val wallpaperUri = data?.data
                   mProfileViewModel.updateUserWallpaper(context!!, wallpaperUri!!)
               }
           }
        }
    }
}