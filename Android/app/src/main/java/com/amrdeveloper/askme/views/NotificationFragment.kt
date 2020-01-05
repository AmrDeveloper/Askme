package com.amrdeveloper.askme.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.askme.viewmodels.NotificationViewModel
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.adapter.NotificationAdapter
import com.amrdeveloper.askme.models.Action
import com.amrdeveloper.askme.models.Constants
import com.amrdeveloper.askme.models.Notification
import com.amrdeveloper.askme.databinding.ListLayoutBinding
import com.amrdeveloper.askme.di.ViewModelProviderFactory
import com.amrdeveloper.askme.extensions.gone
import com.amrdeveloper.askme.extensions.openFragmentInto
import com.amrdeveloper.askme.extensions.show
import com.amrdeveloper.askme.utils.Session
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class NotificationFragment: DaggerFragment(){

    private lateinit var mListLayoutBinding: ListLayoutBinding
    @Inject lateinit var mNotificationAdapter: NotificationAdapter
    private lateinit var mNotificationViewModel: NotificationViewModel
    @Inject lateinit var providerFactory : ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mListLayoutBinding = DataBindingUtil.inflate(inflater,R.layout.list_layout, container, false)
        mNotificationViewModel = ViewModelProviders.of(this, providerFactory).get(NotificationViewModel::class.java)

        notiListSetup()

        val id = Session.getUserId(context!!).toString()
        val token = Session.getUserToken(context!!).toString()

        mListLayoutBinding.loadingBar.show()

        mNotificationViewModel.loadUserNotifications(id, token)

        mNotificationViewModel.getNotificationList().observe(this, Observer {
            mNotificationAdapter.submitList(it)
            mListLayoutBinding.loadingBar.gone()
        })

        return mListLayoutBinding.root
    }

    private fun notiListSetup(){
        mListLayoutBinding.listItems.setHasFixedSize(true)
        mListLayoutBinding.listItems.layoutManager = LinearLayoutManager(context)
        mListLayoutBinding.listItems.adapter = mNotificationAdapter

        mNotificationAdapter.setOnItemClickListener(object : NotificationAdapter.OnItemClickListener {
            override fun onItemClick(notification: Notification) {
                when(notification.action){
                    Action.QUESTION -> {
                        val answerQuestionFragment = AnswerQuestionFragment()

                        val args = Bundle()
                        args.putString(Constants.QUESTION_ID, notification.data)
                        answerQuestionFragment.arguments = args

                        fragmentManager?.openFragmentInto(R.id.viewContainers, answerQuestionFragment)
                    }
                }
            }
        })
    }
}