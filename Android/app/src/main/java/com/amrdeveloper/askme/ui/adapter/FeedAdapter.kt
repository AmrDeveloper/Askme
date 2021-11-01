package com.amrdeveloper.askme.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.Anonymously
import com.amrdeveloper.askme.data.Feed
import com.amrdeveloper.askme.data.Reaction
import com.amrdeveloper.askme.databinding.FeedListItemBinding
import com.amrdeveloper.askme.utils.*

class FeedAdapter : PagedListAdapter<Feed, FeedAdapter.FeedViewHolder>(DIFF_CALL_BACK) {

    interface OnReactionClick{
        fun onReactClick(answerId : Int,toUser : String, reaction : Reaction, callback : Callback)
    }

    interface OnUsernameClick{
        fun onUserClick(userId : String)
    }

    interface Callback{
        fun onCallback()
    }

    private lateinit var onReactionClick : OnReactionClick
    private lateinit var onUsernameClick : OnUsernameClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = FeedListItemBinding.inflate(inflater, parent, false)
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val currentFeed : Feed = getItem(position) ?: return
        currentFeed.notNull {
            holder.bingFeed(currentFeed)
            if(::onUsernameClick.isInitialized){
                if(currentFeed.anonymously == Anonymously.NOT_ANONYMOSLY) {
                    holder.binding.questionFrom.setOnClickListener {
                        onUsernameClick.onUserClick(currentFeed.toUserId.str())
                    }
                }

                holder.binding.answerFrom.setOnClickListener {
                    onUsernameClick.onUserClick(currentFeed.fromUserId.str())
                }
            }

            if(::onReactionClick.isInitialized){
                holder.binding.reactionsTxt.setOnClickListener {
                    onReactionClick.onReactClick(currentFeed.answerId, currentFeed.toUserId.str(), currentFeed.isReacted,
                        object : Callback {
                            override fun onCallback() {
                                when (currentFeed.isReacted) {
                                    Reaction.REACATED -> {
                                        currentList?.get(position)?.reactionsNum =
                                            currentList?.get(position)?.reactionsNum?.minus(1)!!
                                        currentList?.get(position)?.isReacted =
                                            Reaction.UN_REACATED
                                    }
                                    Reaction.UN_REACATED -> {
                                        currentList?.get(position)?.reactionsNum =
                                            currentList?.get(position)?.reactionsNum?.plus(1)!!
                                        currentList?.get(position)?.isReacted =
                                            Reaction.REACATED
                                    }
                                }
                                notifyDataSetChanged()
                            }
                        })
                }
            }
        }
    }

    fun setOnReactionListener(listener : OnReactionClick){
        onReactionClick = listener
    }

    fun setOnUsernameListener(listener : OnUsernameClick){
        onUsernameClick = listener
    }

    class FeedViewHolder(val binding : FeedListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bingFeed(feed: Feed) {
            binding.questionTxt.text = feed.questionBody
            binding.answerTxt.text = feed.answerBody

            if (feed.anonymously == Anonymously.NOT_ANONYMOSLY) {
                binding.questionFrom.text = feed.toUserName
                binding.questionUserAvatar.loadImage(feed.toUserAvatar, R.drawable.ic_profile)
            } else {
                binding.questionFrom.text = "Anonymous user"
            }

            binding.answerFrom.text = feed.fromUserName
            binding.answerDateTxt.setFormattedDateForPost(feed.answerDate)
            binding.reactionsTxt.setTextOrHide(feed.reactionsNum.toString())

            binding.answerUserAvatar.loadImage(feed.fromUserAvatar, R.drawable.ic_profile)

            updateReactedIcon(feed.isReacted)
        }

        private fun updateReactedIcon(reaction: Reaction){
            when(reaction){
                Reaction.REACATED -> {
                    binding.reactionsTxt.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange))
                    binding.reactionsTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_reacted,0,0,0)
                }
                Reaction.UN_REACATED -> {
                    binding.reactionsTxt.setTextColor(ContextCompat.getColor(itemView.context ,R.color.black))
                    binding.reactionsTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_react,0,0,0)
                }
            }
        }
    }
}

private val DIFF_CALL_BACK : DiffUtil.ItemCallback<Feed> = object : DiffUtil.ItemCallback<Feed>(){
    override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
        return oldItem.answerId == newItem.answerId
    }

    override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
        return oldItem == newItem
    }
}