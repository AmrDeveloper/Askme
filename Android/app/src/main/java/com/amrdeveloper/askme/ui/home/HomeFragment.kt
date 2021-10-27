package com.amrdeveloper.askme.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.Constants
import com.amrdeveloper.askme.data.Reaction
import com.amrdeveloper.askme.data.ReactionData
import com.amrdeveloper.askme.databinding.ListLayoutBinding
import com.amrdeveloper.askme.extensions.gone
import com.amrdeveloper.askme.extensions.show
import com.amrdeveloper.askme.extensions.str
import com.amrdeveloper.askme.ui.adapter.FeedAdapter
import com.amrdeveloper.askme.utils.Session
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject lateinit var mFeedAdapter : FeedAdapter
    private lateinit var mListLayoutBinding: ListLayoutBinding

    private val mHomeViewModel  by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mListLayoutBinding = DataBindingUtil.inflate(inflater,R.layout.list_layout, container, false)

        setupUserList()

        mHomeViewModel.loadUserHomeFeed(Session.getUserId(requireContext()).str())

        mListLayoutBinding.loadingBar.show()

        mHomeViewModel.getFeedPagedList().observe(viewLifecycleOwner, {
            mFeedAdapter.submitList(it)
            mListLayoutBinding.loadingBar.gone()
        })

        return mListLayoutBinding.root
    }

    private fun setupUserList(){
        mListLayoutBinding.listItems.setHasFixedSize(true)
        mListLayoutBinding.listItems.layoutManager = LinearLayoutManager(context)
        mListLayoutBinding.listItems.adapter = mFeedAdapter

        mFeedAdapter.setOnUsernameListener(object : FeedAdapter.OnUsernameClick {
            override fun onUserClick(userId: String) {
                val args = Bundle()
                args.putString(Constants.USER_ID, userId)
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment, args)
            }
        })

        mFeedAdapter.setOnReactionListener(object : FeedAdapter.OnReactionClick {
            override fun onReactClick(answerId: Int, toUser : String, reaction: Reaction, callback: FeedAdapter.Callback){
                when(reaction){
                    Reaction.REACATED -> {
                        val token = Session.getHeaderToken(requireContext()).str()
                        val id =  Session.getUserId(requireContext()).str()
                        val body = ReactionData(id, toUser, answerId.str())

                        mHomeViewModel.unreactAnswer(token, body, callback)
                    }
                    Reaction.UN_REACATED -> {
                        val token = Session.getHeaderToken(requireContext()).str()
                        val id =  Session.getUserId(requireContext()).str()
                        val body = ReactionData(id, toUser, answerId.str())

                        mHomeViewModel.reactAnswer(token, body, callback)
                    }
                }
            }
        })
    }
}