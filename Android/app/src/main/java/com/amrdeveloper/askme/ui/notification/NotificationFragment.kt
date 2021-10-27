package com.amrdeveloper.askme.ui.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

@AndroidEntryPoint
class NotificationFragment: Fragment(){

    private lateinit var notificationAdapter: NotificationAdapter

    private lateinit var binding: ListLayoutBinding
    private val viewModel by viewModels<NotificationViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.list_layout, container, false)

        setupNotificationsList()

        val id = Session.getUserId(requireContext()).toString()
        val token = Session.getUserToken(requireContext()).toString()

        viewModel.loadUserNotifications(id, token)

        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.getNotificationList().observe(viewLifecycleOwner, {
            notificationAdapter.submitList(it)
        })
    }

    private fun setupNotificationsList(){
        notificationAdapter = NotificationAdapter()
        binding.listItems.setHasFixedSize(true)
        binding.listItems.layoutManager = LinearLayoutManager(context)
        binding.listItems.adapter = notificationAdapter

        notificationAdapter.setOnItemClickListener(object : NotificationAdapter.OnItemClickListener {
            override fun onItemClick(notification: Notification) {
                if(notification.isOpened == Open.UN_OPENED) {
                    val token = Session.getHeaderToken(requireContext()).str()
                    viewModel.makeNotificationReaded(notification.id.str(), token)
                    notification.isOpened = Open.OPENED
                    notificationAdapter.notifyDataSetChanged()
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