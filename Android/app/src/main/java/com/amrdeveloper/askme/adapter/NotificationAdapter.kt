package com.amrdeveloper.askme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.Notification
import com.amrdeveloper.askme.extensions.notNull
import kotlinx.android.synthetic.main.notification_list_item.view.*

class NotificationAdapter :
    PagedListAdapter<Notification, NotificationAdapter.NotificationViewHolder>(DIFF_CALL_BACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.notification_list_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val currentNotification = getItem(position)
        currentNotification.notNull {
            holder.bingNotification(it)
        }
    }

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bingNotification(notification: Notification) {
            itemView.notificationBody.text = notification.body
            itemView.notificationDate.text = notification.createdDate
            //TODO : Load icon depend on action
        }
    }
}

private val DIFF_CALL_BACK: DiffUtil.ItemCallback<Notification> =
    object : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }