package com.amrdeveloper.askme.views

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.askme.viewmodels.HomeViewModel
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.adapter.FeedAdapter
import com.amrdeveloper.askme.models.Constants
import com.amrdeveloper.askme.models.Reaction
import com.amrdeveloper.askme.models.ReactionData
import com.amrdeveloper.askme.databinding.ListLayoutBinding
import com.amrdeveloper.askme.extensions.gone
import com.amrdeveloper.askme.extensions.show
import com.amrdeveloper.askme.extensions.str
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

        mHomeViewModel.loadUserHomeFeed(Session.getUserId(context!!).str())

        mListLayoutBinding.loadingBar.show()

        mHomeViewModel.getFeedPagedList().observe(this, Observer {
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
                        val token = Session.getHeaderToken(context!!).str()
                        val id =  Session.getUserId(context!!).str()
                        val body = ReactionData(id, toUser, answerId.str())

                        mHomeViewModel.unreactAnswer(token, body, callback)
                    }
                    Reaction.UN_REACATED -> {
                        val token = Session.getHeaderToken(context!!).str()
                        val id =  Session.getUserId(context!!).str()
                        val body = ReactionData(id, toUser, answerId.str())

                        mHomeViewModel.reactAnswer(token, body, callback)
                    }
                }
            }
        })
    }
}