package com.amrdeveloper.askme.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.Action
import com.amrdeveloper.askme.data.Constants
import com.amrdeveloper.askme.data.Notification
import com.amrdeveloper.askme.data.Open
import com.amrdeveloper.askme.databinding.ListLayoutBinding
import com.amrdeveloper.askme.utils.gone
import com.amrdeveloper.askme.utils.show
import com.amrdeveloper.askme.utils.str
import com.amrdeveloper.askme.ui.adapter.NotificationAdapter
import com.amrdeveloper.askme.utils.Session
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment: Fragment(){

    private lateinit var mListLayoutBinding: ListLayoutBinding
    @Inject lateinit var mNotificationAdapter: NotificationAdapter

    private val mNotificationViewModel by viewModels<NotificationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mListLayoutBinding = DataBindingUtil.inflate(inflater,R.layout.list_layout, container, false)

        notiListSetup()

        val id = Session.getUserId(requireContext()).toString()
        val token = Session.getUserToken(requireContext()).toString()

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
                if(notification.isOpened == Open.UN_OPENED) {
                    val token = Session.getHeaderToken(requireContext()).str()
                    mNotificationViewModel.makeNotificationReaded(notification.id.str(), token)
                    notification.isOpened = Open.OPENED
                    mNotificationAdapter.notifyDataSetChanged()
                }

                if (notification.action == Action.QUESTION) {
                    val bundle = bundleOf(Constants.QUESTION_ID to notification.data)
                    findNavController().navigate(R.id.action_notificationFragment_to_answerQuestionFragment, bundle)
                }
                else if (notification.action == Action.ANSWER) {
                    val bundle = bundleOf(Constants.ANSWER_ID to notification.data)
                    findNavController().navigate(R.id.action_notificationFragment_to_questionAnswerFragment, bundle)
                }
            }
        })
    }
}