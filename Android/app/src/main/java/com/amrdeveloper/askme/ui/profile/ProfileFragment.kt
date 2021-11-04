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
import com.amrdeveloper.askme.data.*
import com.amrdeveloper.askme.databinding.ProfileLayoutBinding
import com.amrdeveloper.askme.ui.adapter.FeedAdapter
import com.amrdeveloper.askme.utils.*
import dagger.hilt.android.AndroidEntryPoint

private const val REQUEST_AVATAR_ID = 1996
private const val REQUEST_WALLPAPER_ID = 1997
private const val PERMISSION_EXTERNAL_STORAGE = 1998

@AndroidEntryPoint
class ProfileFragment : Fragment(){

    private lateinit var currentUserId: String
    private lateinit var currentUser : User

    private lateinit var feedAdapter: FeedAdapter

    private var _binding: ProfileLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.get(USER_ID)?.let { currentUserId = it as String }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.profile_layout, container, false)

        updateUserInfoFromArguments()
        setupFeedRecyclerView()

        binding.listLayout.loadingBar.show()

        viewModel.loadUserInformation(currentUserId, Session.getUserId(requireContext()).toString())
        viewModel.loadUserFeed(currentUserId, Session.getUserId(requireContext()).toString())

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
        viewModel.getUserLiveData().observe(viewLifecycleOwner, {
            currentUser = it
            bindUserProfile(it)
        })

        viewModel.getFeedPagedList().observe(viewLifecycleOwner, {
            feedAdapter.submitData(lifecycle, it)
            binding.listLayout.loadingBar.gone()
        })

        viewModel.messages.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        viewModel.getFollowLiveData().observe(viewLifecycleOwner, {updateFollowCardView(it)})

        binding.askmeButton.setOnClickListener {openAskQuestionFragment()}

        binding.askmeFollowMe.setOnClickListener {followCardViewListener()}
    }

    private fun updateUserInfoFromArguments(){
        currentUserId = arguments?.getString(USER_ID).toString()
        if (currentUserId.isEmpty() || currentUserId == "null") {
            currentUserId = Session.getUserId(requireContext()).toString()
            setupEditMode()
        }else{
            hideEditMode()
        }
    }

    private fun setupFeedRecyclerView() {
        feedAdapter = FeedAdapter()
        binding.listLayout.listItems.setHasFixedSize(true)
        binding.listLayout.listItems.layoutManager = LinearLayoutManager(context)
        binding.listLayout.listItems.isNestedScrollingEnabled = false
        binding.listLayout.listItems.adapter = feedAdapter
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
            val bundle = bundleOf(AVATAR_URL to currentUser.avatarUrl)
            findNavController().navigate(R.id.action_profileFragment_to_fullscreenFragment, bundle)
        }

        binding.userWallpaper.setOnClickListener {
            val bundle = bundleOf(AVATAR_URL to currentUser.wallpaperUrl)
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
        viewModel.getFollowLiveData().removeObservers(this)
    }

    private fun openAskQuestionFragment(){
        val bundle = Bundle()
        bundle.putString(USER_ID, currentUser.id)
        bundle.putString(NAME, currentUser.name)
        bundle.putString(USERNAME, currentUser.username)
        bundle.putString(AVATAR_URL, currentUser.avatarUrl)

        findNavController().navigate(R.id.action_profileFragment_to_askQuestionFragment, bundle)
    }

    private fun followCardViewListener(){
        val followData = FollowData(Session.getUserId(requireContext()).toString(), currentUserId)
        val token = Session.getUserToken(requireContext()).toString()

        when (Follow.valueOf(binding.askmeFollowMe.tag.toString())) {
            Follow.FOLLOW -> viewModel.unfollowUser(token ,followData)
            Follow.UN_FOLLOW -> viewModel.followUser(token ,followData)
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
                   viewModel.updateUserAvatar(requireContext(), avatarUri!!)
               }
               REQUEST_WALLPAPER_ID -> {
                   val wallpaperUri = data?.data
                   viewModel.updateUserWallpaper(requireContext(), wallpaperUri!!)
               }
           }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}