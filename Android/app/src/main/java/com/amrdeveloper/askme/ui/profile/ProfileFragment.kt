package com.amrdeveloper.askme.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.Constants
import com.amrdeveloper.askme.data.Follow
import com.amrdeveloper.askme.data.FollowData
import com.amrdeveloper.askme.data.User
import com.amrdeveloper.askme.data.remote.net.ResponseType
import com.amrdeveloper.askme.databinding.ProfileLayoutBinding
import com.amrdeveloper.askme.ui.adapter.FeedAdapter
import com.amrdeveloper.askme.utils.*
import dagger.hilt.android.AndroidEntryPoint

private const val REQUEST_AVATAR_ID = 1996
private const val REQUEST_WALLPAPER_ID = 1997
private const val PERMISSION_EXTERNAL_STORAGE = 1998

@AndroidEntryPoint
class ProfileFragment : Fragment(){

    private lateinit var mUserId: String
    private lateinit var mCurrentUser : User

    lateinit var mFeedAdapter: FeedAdapter

    private var _binding: ProfileLayoutBinding? = null
    private val binding get() = _binding!!

    private val mProfileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.get(Constants.USER_ID)?.let { mUserId = it as String }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.profile_layout, container, false)

        updateUserInfoFromArguments()
        setupFeedRecyclerView()

        binding.listLayout.loadingBar.show()

        mProfileViewModel.loadUserInformation(mUserId, Session.getUserId(requireContext()).toString())
        mProfileViewModel.loadUserFeed(mUserId, Session.getUserId(requireContext()).toString())

        setupObservers()

        setupFullScreenOption()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logoutMenu -> {
                Session.logout(requireContext())
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }
            R.id.settingsMenu -> {
                findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupObservers( ) {
        mProfileViewModel.getUserLiveData().observe(viewLifecycleOwner, {
            mCurrentUser = it
            bindUserProfile(it)
        })

        mProfileViewModel.getFeedPagedList().observe(viewLifecycleOwner, {
            mFeedAdapter.submitList(it)
            binding.listLayout.loadingBar.gone()
        })

        mProfileViewModel.getAvatarLiveData().observe(viewLifecycleOwner, {
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

        mProfileViewModel.getWallpaperLiveData().observe(viewLifecycleOwner, {
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

        mProfileViewModel.getFollowLiveData().observe(viewLifecycleOwner, {updateFollowCardView(it)})

        binding.askmeButton.setOnClickListener {openAskQuestionFragment()}

        binding.askmeFollowMe.setOnClickListener {followCardViewListener()}
    }

    private fun updateUserInfoFromArguments(){
        mUserId = arguments?.getString(Constants.USER_ID).toString()
        if (mUserId.isEmpty() || mUserId == "null") {
            mUserId = Session.getUserId(requireContext()).toString()
            setupEditMode()
        }else{
            hideEditMode()
        }
    }

    private fun setupFeedRecyclerView() {
        mFeedAdapter = FeedAdapter()
        binding.listLayout.listItems.setHasFixedSize(true)
        binding.listLayout.listItems.layoutManager = LinearLayoutManager(context)
        binding.listLayout.listItems.isNestedScrollingEnabled = false
        binding.listLayout.listItems.adapter = mFeedAdapter
    }

    private fun bindUserProfile(user: User) {
        binding.userName.setTextOrGone(user.username)
        binding.userAddress.setTextOrGone("Lived in ${user.address}")
        binding.userStatus.setTextOrGone(user.status)
        binding.userJoinDate.setFormattedJoinDate(user.joinDate)

        binding.analysisCardLayout.userFollowing.text = user.followingNum.toString()
        binding.analysisCardLayout.userFollowers.text = user.followersNum.toString()
        binding.analysisCardLayout.userLikes.text = user.reactionsNum.toString()
        binding.analysisCardLayout.userQuestions.text = user.questionsNum.toString()
        binding.analysisCardLayout.userAnswers.text = user.answersNum.toString()

        binding.userAvatar.loadImage(user.avatarUrl, R.drawable.ic_profile)
        binding.userWallpaper.loadImage(user.wallpaperUrl, R.drawable.ic_wallpaper)

        if (user.id != Session.getUserId(requireContext())) {
            updateFollowCardView(user.isUserFollow)
        } else {
            binding.askmeFollowMe.gone()
        }
    }

    private fun setupFullScreenOption(){
        binding.userAvatar.setOnClickListener {
            val bundle = bundleOf(Constants.AVATAR_URL to mCurrentUser.avatarUrl)
            findNavController().navigate(R.id.action_profileFragment_to_fullscreenFragment, bundle)
        }

        binding.userWallpaper.setOnClickListener {
            val bundle = bundleOf(Constants.AVATAR_URL to mCurrentUser.wallpaperUrl)
            findNavController().navigate(R.id.action_profileFragment_to_fullscreenFragment, bundle)
        }
    }

    private fun updateFollowCardView(follow: Follow) {
        when (follow) {
            Follow.FOLLOW -> {
                binding.askmeFollowMe.text = getString(R.string.following)
                binding.askmeFollowMe.tag = Follow.FOLLOW
            }
            Follow.UN_FOLLOW -> {
                binding.askmeFollowMe.text = getString(R.string.follow)
                binding.askmeFollowMe.tag = Follow.UN_FOLLOW
            }
        }
    }

    private fun setupEditMode(){
        binding.changeAvatarAction.setOnClickListener { changeAvatarImage() }
        binding.changeWallpaperAction.setOnClickListener { changeWallpaperImage() }
    }

    private fun hideEditMode(){
        binding.changeAvatarAction.gone()
        binding.changeWallpaperAction.gone()
        mProfileViewModel.getFollowLiveData().removeObservers(this)
        mProfileViewModel.getAvatarLiveData().removeObservers(this)
        mProfileViewModel.getWallpaperLiveData().removeObservers(this)
    }

    private fun openAskQuestionFragment(){
        val bundle = Bundle()
        bundle.putString(Constants.USER_ID, mCurrentUser.id)
        bundle.putString(Constants.NAME, mCurrentUser.name)
        bundle.putString(Constants.USERNAME, mCurrentUser.username)
        bundle.putString(Constants.AVATAR_URL, mCurrentUser.avatarUrl)

        findNavController().navigate(R.id.action_profileFragment_to_askQuestionFragment, bundle)
    }

    private fun followCardViewListener(){
        val followData = FollowData(Session.getUserId(requireContext()).toString(), mUserId)
        val token = Session.getUserToken(requireContext()).toString()

        when (Follow.valueOf(binding.askmeFollowMe.tag.toString())) {
            Follow.FOLLOW -> mProfileViewModel.unfollowUser(token ,followData)
            Follow.UN_FOLLOW -> mProfileViewModel.followUser(token ,followData)
        }
    }

    private fun changeAvatarImage(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, REQUEST_AVATAR_ID)
        } else{
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_EXTERNAL_STORAGE)
        }
    }

    private fun changeWallpaperImage(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, REQUEST_WALLPAPER_ID)
        } else{
            ActivityCompat.requestPermissions(requireActivity(),
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
                   mProfileViewModel.updateUserAvatar(requireContext(), avatarUri!!)
               }
               REQUEST_WALLPAPER_ID -> {
                   val wallpaperUri = data?.data
                   mProfileViewModel.updateUserWallpaper(requireContext(), wallpaperUri!!)
               }
           }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}