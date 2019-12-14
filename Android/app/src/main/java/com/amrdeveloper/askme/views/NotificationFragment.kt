package com.amrdeveloper.askme.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.askme.models.NotificationViewModel
import com.amrdeveloper.askme.presenters.NotificationPresenter
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.adapter.NotificationAdapter
import com.amrdeveloper.askme.contracts.NotificationContract
import com.amrdeveloper.askme.data.Notification
import com.amrdeveloper.askme.events.LoadFinishEvent
import com.amrdeveloper.askme.extensions.gone
import com.amrdeveloper.askme.extensions.show
import com.amrdeveloper.askme.utils.AskmeFragment
import com.amrdeveloper.askme.utils.Session
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NotificationFragment: AskmeFragment() , NotificationContract.View{

    private lateinit var loadingBar : ProgressBar
    private lateinit var mNotiPresenter: NotificationPresenter
    private lateinit var mNotiAdapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_layout, container, false)

        notiListSetup(view)
        loadingBar = view.findViewById(R.id.loadingBar)

        NotificationViewModel.setUserId(Session().getUserId(context!!).toString())
        NotificationViewModel.setToken(Session().getUserToken(context!!).toString())
        val notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel::class.java)

        mNotiPresenter = NotificationPresenter(this, notificationViewModel ,this )

        mNotiPresenter.startLoadingNotifications()

        return view
    }

    private fun notiListSetup(view : View){
        mNotiAdapter = NotificationAdapter()
        val listItems  = view.findViewById<RecyclerView>(R.id.listItems)
        listItems.setHasFixedSize(true)
        listItems.layoutManager = LinearLayoutManager(context)
        listItems.adapter = mNotiAdapter
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoadFinishEvent(event : LoadFinishEvent<PagedList<Notification>>){
        mNotiAdapter.submitList(event.data)
        hideProgressBar()
    }

    override fun showProgressBar() {
        loadingBar.show()
    }

    override fun hideProgressBar() {
        loadingBar.gone()
    }
}