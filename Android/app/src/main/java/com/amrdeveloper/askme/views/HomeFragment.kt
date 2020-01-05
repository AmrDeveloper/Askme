package com.amrdeveloper.askme.views

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.askme.viewmodels.HomeViewModel
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.adapter.FeedAdapter
import com.amrdeveloper.askme.models.Constants
import com.amrdeveloper.askme.models.Reaction
import com.amrdeveloper.askme.models.ReactionData
import com.amrdeveloper.askme.databinding.ListLayoutBinding
import com.amrdeveloper.askme.di.ViewModelProviderFactory
import com.amrdeveloper.askme.extensions.gone
import com.amrdeveloper.askme.extensions.openFragmentInto
import com.amrdeveloper.askme.extensions.show
import com.amrdeveloper.askme.extensions.str
import com.amrdeveloper.askme.utils.Session
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

    @Inject lateinit var mFeedAdapter : FeedAdapter
    private lateinit var mHomeViewModel : HomeViewModel
    private lateinit var mListLayoutBinding: ListLayoutBinding
    @Inject lateinit var providerFactory : ViewModelProviderFactory

    private val LOG_TAG = "HomeFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mListLayoutBinding = DataBindingUtil.inflate(inflater,R.layout.list_layout, container, false)
        mHomeViewModel = ViewModelProviders.of(this, providerFactory).get(HomeViewModel::class.java)

        setupUserList()

        mHomeViewModel.loadUserHomeFeed("15")
        //val session = Session()
        //mHomeViewModel.loadUserHomeFeed(session.getUserId(context!!).str())

        mListLayoutBinding.loadingBar.show()

        mHomeViewModel.getFeedPagedList().observe(this, Observer {
            mFeedAdapter.submitList(it)
            mListLayoutBinding.loadingBar.gone()
        })

        return mListLayoutBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settingsMenu){

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupUserList(){
        mListLayoutBinding.listItems.setHasFixedSize(true)
        mListLayoutBinding.listItems.layoutManager = LinearLayoutManager(context)
        mListLayoutBinding.listItems.adapter = mFeedAdapter

        mFeedAdapter.setOnUsernameListener(object : FeedAdapter.OnUsernameClick {
            override fun onUserClick(userId: String) {
                val profileFragment = ProfileFragment()

                val args = Bundle()
                args.putString(Constants.USER_ID, userId)
                profileFragment.arguments = args

                fragmentManager?.openFragmentInto(R.id.viewContainers, profileFragment)
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