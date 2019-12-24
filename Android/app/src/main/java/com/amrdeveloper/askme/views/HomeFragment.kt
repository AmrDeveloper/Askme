package com.amrdeveloper.askme.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.askme.presenters.HomePresenter
import com.amrdeveloper.askme.models.HomeViewModel
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.adapter.FeedAdapter
import com.amrdeveloper.askme.contracts.HomeContract
import com.amrdeveloper.askme.data.Constants
import com.amrdeveloper.askme.data.Feed
import com.amrdeveloper.askme.data.Reaction
import com.amrdeveloper.askme.data.ReactionData
import com.amrdeveloper.askme.events.LoadFinishEvent
import com.amrdeveloper.askme.extensions.gone
import com.amrdeveloper.askme.extensions.openFragmentInto
import com.amrdeveloper.askme.extensions.show
import com.amrdeveloper.askme.extensions.str
import com.amrdeveloper.askme.net.AskmeClient
import com.amrdeveloper.askme.utils.Session
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), HomeContract.View {

    private lateinit var mLoadingBar: ProgressBar
    private lateinit var mFeedAdapter : FeedAdapter
    private lateinit var mHomePresenter: HomePresenter

    private val LOG_TAG = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_layout, container, false)
        mLoadingBar = view.findViewById(R.id.loadingBar)
        setupUserList(view)

        val session = Session()
        HomeViewModel.setUserId("15")
        //HomeViewModel.setUserId(session.getUserId(context!!).toString())
        val homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        mHomePresenter = HomePresenter(this,  homeViewModel,this )

        mHomePresenter.startLoadingHomeFeed()

        return view
    }

    private fun setupUserList(view : View){
        mFeedAdapter = FeedAdapter()
        val listItems  = view.findViewById<RecyclerView>(R.id.listItems)
        listItems.setHasFixedSize(true)
        listItems.layoutManager = LinearLayoutManager(context)
        listItems.adapter = mFeedAdapter

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
            override fun onReactClick(answerId: Int, reaction: Reaction, callback: FeedAdapter.Callback){
                when(reaction){
                    Reaction.REACATED -> {
                        val body = ReactionData(Session().getUserId(context!!).str(), answerId.str())
                        AskmeClient.getReactionService()
                            .deleteAnswerReaction(
                                token = Session().getHeaderToken(context!!).str(),
                                reactionData = body
                            ).enqueue(object : Callback<String> {
                                override fun onResponse(
                                    call: Call<String>,
                                    response: Response<String>
                                ) {
                                    if(response.code() == 200){
                                        callback.onCallback(true)
                                    }else{
                                        callback.onCallback(false)
                                    }
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Log.d(LOG_TAG, "Can't react this post")
                                    callback.onCallback(false)
                                }
                            })
                    }
                    Reaction.UN_REACATED -> {
                        val body = ReactionData(Session().getUserId(context!!).str(), answerId.str())
                        AskmeClient.getReactionService()
                            .createAnswerReaction(
                                token = Session().getHeaderToken(context!!).str(),
                                reactionData = body
                            ).enqueue(object : Callback<String> {
                                override fun onResponse(
                                    call: Call<String>,
                                    response: Response<String>
                                ) {
                                    if(response.code() == 200){
                                        callback.onCallback(true)
                                    }else{
                                        callback.onCallback(false)
                                    }
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Log.d(LOG_TAG, "Can't react this post")
                                    callback.onCallback(false)
                                }
                            })
                    }
                }
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoadFinishEvent(event : LoadFinishEvent<PagedList<Feed>>){
        mFeedAdapter.submitList(event.data)
        hideProgressBar()
    }

    override fun showProgressBar() {
        mLoadingBar.show()
    }

    override fun hideProgressBar() {
        mLoadingBar.gone()
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