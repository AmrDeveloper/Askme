package com.amrdeveloper.askme.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.askme.PeopleContract
import com.amrdeveloper.askme.PeoplePresenter
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.UserViewModel
import com.amrdeveloper.askme.adapter.PeopleAdapter
import com.amrdeveloper.askme.data.User
import com.amrdeveloper.askme.events.LoadFinishEvent
import com.amrdeveloper.askme.extensions.gone
import com.amrdeveloper.askme.extensions.show
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PeopleFragment : Fragment(), PeopleContract.View{

    private lateinit var mLoadingBar : ProgressBar
    private lateinit var mUserAdapter : PeopleAdapter
    private lateinit var mPeoplePresenter: PeoplePresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_layout, container, false)
        mLoadingBar = view.findViewById(R.id.loadingBar)
        setupUserList(view)

        val userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        mPeoplePresenter = PeoplePresenter(this, userViewModel, this)
        mPeoplePresenter.startLoadingPeople()

        return view
    }

    private fun setupUserList(view : View){
        mUserAdapter = PeopleAdapter()
        val listItems  = view.findViewById<RecyclerView>(R.id.listItems)
        listItems.setHasFixedSize(true)
        listItems.layoutManager = LinearLayoutManager(context)
        listItems.adapter = mUserAdapter
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoadFinishEvent(event : LoadFinishEvent<PagedList<User>>){
        mUserAdapter.submitList(event.data)
        hideProgressBar()
    }

    override fun showProgressBar() {
        mLoadingBar.gone()
    }

    override fun hideProgressBar() {
        mLoadingBar.show()
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