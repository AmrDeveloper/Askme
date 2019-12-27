package com.amrdeveloper.askme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.models.User
import com.amrdeveloper.askme.extensions.loadImage
import com.amrdeveloper.askme.extensions.notNull
import kotlinx.android.synthetic.main.user_list_item.view.*

class PeopleAdapter : PagedListAdapter<User, PeopleAdapter.UserViewHolder>(DIFF_CALL_BACK){

    interface OnUserClickListener{
        fun onClick(user : User)
    }

    private lateinit var mOnclickListener : OnUserClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_list_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        user.notNull {
            holder.bingUser(user!!)
            holder.itemView.setOnClickListener {
                if(::mOnclickListener.isInitialized){
                    mOnclickListener.onClick(user)
                }
            }
        }
    }

    fun setOnUserClickListener(listener: OnUserClickListener){
        mOnclickListener = listener
    }

    class UserViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun bingUser(user : User){
            itemView.usernameTxt.text = user.username
            itemView.reactionsTxt.text = user.reactionsNum.toString()

            itemView.userAvatar.loadImage(user.avatarUrl)
            //TODO : Change Border Color depend on user fav color
        }
    }
}

private val DIFF_CALL_BACK: DiffUtil.ItemCallback<User> =
    object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }