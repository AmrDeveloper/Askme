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
import com.amrdeveloper.askme.data.Feed
import com.amrdeveloper.askme.data.Reaction
import com.amrdeveloper.askme.data.ReactionData
import com.amrdeveloper.askme.databinding.ListLayoutBinding
import com.amrdeveloper.askme.utils.gone
import com.amrdeveloper.askme.utils.show
import com.amrdeveloper.askme.utils.str
import com.amrdeveloper.askme.ui.adapter.FeedAdapter
import com.amrdeveloper.askme.utils.Session
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var feedAdapter : FeedAdapter
    private lateinit var binding: ListLayoutBinding

    private val viewModel  by viewModels<HomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.list_layout, container, false)

        setupUserList()

        viewModel.loadUserHomeFeed(Session.getUserId(requireContext()).str())

        setupObservers()

        return binding.root
    }

    private fun setupUserList(){
        feedAdapter = FeedAdapter()
        binding.listItems.setHasFixedSize(true)
        binding.listItems.layoutManager = LinearLayoutManager(context)
        binding.listItems.adapter = feedAdapter

        feedAdapter.setOnUsernameListener(object : FeedAdapter.OnUsernameClick {
            override fun onUserClick(userId: String) {
                val args = Bundle()
                args.putString(Constants.USER_ID, userId)
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment, args)
            }
        })

        feedAdapter.setOnReactionListener(object : FeedAdapter.OnReactionClick {
            override fun onReactClick(answerId: Int, toUser : String, reaction: Reaction, callback: FeedAdapter.Callback){
                when(reaction){
                    Reaction.REACATED -> {
                        val token = Session.getHeaderToken(requireContext()).str()
                        val id =  Session.getUserId(requireContext()).str()
                        val body = ReactionData(id, toUser, answerId.str())

                        viewModel.unreactAnswer(token, body, callback)
                    }
                    Reaction.UN_REACATED -> {
                        val token = Session.getHeaderToken(requireContext()).str()
                        val id =  Session.getUserId(requireContext()).str()
                        val body = ReactionData(id, toUser, answerId.str())

                        viewModel.reactAnswer(token, body, callback)
                    }
                }
            }
        })
    }

    private fun setupObservers() {
        viewModel.getFeedPagedList().observe(viewLifecycleOwner, {
            feedAdapter.submitList(it)
            binding.loadingBar.gone()
        })
    }
}