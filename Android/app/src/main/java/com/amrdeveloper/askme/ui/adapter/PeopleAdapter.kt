package com.amrdeveloper.askme.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.User
import com.amrdeveloper.askme.databinding.UserListItemBinding
import com.amrdeveloper.askme.utils.loadImage
import com.amrdeveloper.askme.utils.notNull

class PeopleAdapter : PagedListAdapter<User, PeopleAdapter.UserViewHolder>(DIFF_CALL_BACK){

    interface OnUserClickListener{
        fun onClick(user : User)
    }

    private lateinit var mOnclickListener : OnUserClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = UserListItemBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        user.notNull {
            holder.bingUser(user!!)
            holder.itemView.setOnClickListener {
                if(::mOnclickListener.isInitialized){
                    Log.d("Adapter","Avatar : ${user.avatarUrl}")
                    mOnclickListener.onClick(user)
                }
            }
        }
    }

    fun setOnUserClickListener(listener: OnUserClickListener){
        mOnclickListener = listener
    }

    class UserViewHolder(val binding : UserListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bingUser(user : User){
            binding.usernameTxt.text = user.username
            binding.reactionsTxt.text = user.reactionsNum.toString()
            binding.userAvatar.loadImage(user.avatarUrl, R.drawable.ic_profile)
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