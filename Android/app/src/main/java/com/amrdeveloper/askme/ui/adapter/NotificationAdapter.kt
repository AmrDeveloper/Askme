package com.amrdeveloper.askme.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.Action
import com.amrdeveloper.askme.data.Notification
import com.amrdeveloper.askme.data.Open
import com.amrdeveloper.askme.extensions.backgroundColor
import com.amrdeveloper.askme.extensions.notNull
import com.amrdeveloper.askme.extensions.setFormattedDateForPost
import kotlinx.android.synthetic.main.notification_list_item.view.*

class NotificationAdapter :
    PagedListAdapter<Notification, NotificationAdapter.NotificationViewHolder>(DIFF_CALL_BACK) {

    interface OnItemClickListener{
        fun onItemClick(notification: Notification)
    }

    private lateinit var mItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.notification_list_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val currentNotification = getItem(position)
        currentNotification.notNull {
            holder.bingNotification(it)
            if(::mItemClickListener.isInitialized){
                holder.itemView.setOnClickListener {
                    mItemClickListener.onItemClick(currentNotification!!)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mItemClickListener = listener
    }

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bingNotification(notification: Notification) {
            itemView.notificationBody.text = notification.body
            itemView.notificationDate.setFormattedDateForPost(notification.createdDate)

            when(notification.action){
                Action.QUESTION -> itemView.notificationIcon.setImageResource(R.drawable.ic_question)
                Action.ANSWER -> itemView.notificationIcon.setImageResource(R.drawable.ic_answer)
                Action.FOLLOW -> itemView.notificationIcon.setImageResource(R.drawable.ic_followers)
            }

            if(notification.isOpened == Open.UN_OPENED){
                itemView.notificationCardView.backgroundColor(itemView.context, R.color.whiteOrange)
            }
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