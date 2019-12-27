package com.amrdeveloper.askme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.models.Feed
import com.amrdeveloper.askme.models.Reaction
import com.amrdeveloper.askme.extensions.*
import kotlinx.android.synthetic.main.feed_list_item.view.*

class FeedAdapter : PagedListAdapter<Feed, FeedAdapter.FeedViewHolder>(DIFF_CALL_BACK) {

    interface OnReactionClick{
        fun onReactClick(answerId : Int,toUser : String, reaction : Reaction, callback : Callback)
    }

    interface OnUsernameClick{
        fun onUserClick(userId : String)
    }

    interface Callback{
        fun onCallback(state : Boolean)
    }

    private lateinit var onReactionClick : OnReactionClick
    private lateinit var onUsernameClick : OnUsernameClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.feed_list_item, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val currentFeed : Feed? = getItem(position)
        currentFeed.notNull {
            holder.bingFeed(currentFeed!!)
            if(::onUsernameClick.isInitialized){
                holder.itemView.questionFrom.setOnClickListener {
                    onUsernameClick.onUserClick(currentFeed.toUserId.str())
                }

                holder.itemView.answerFrom.setOnClickListener {
                    onUsernameClick.onUserClick(currentFeed.fromUserId.str())
                }
            }

            if(::onReactionClick.isInitialized){
                holder.itemView.reactionsTxt.setOnClickListener {
                    onReactionClick.onReactClick(
                        currentFeed.answerId, currentFeed.toUserId.str(), currentFeed.isReacted,
                        object : Callback {
                            override fun onCallback(state: Boolean) {
                                if (state) {
                                    when (currentFeed.isReacted) {
                                        Reaction.REACATED -> {
                                            currentList?.get(position)?.reactionsNum =
                                                currentList?.get(position)?.reactionsNum?.plus(1)!!
                                            currentList?.get(position)?.isReacted =
                                                Reaction.UN_REACATED
                                        }
                                        Reaction.UN_REACATED -> {
                                            currentList?.get(position)?.reactionsNum =
                                                currentList?.get(position)?.reactionsNum?.minus(1)!!
                                            currentList?.get(position)?.isReacted =
                                                Reaction.REACATED
                                        }
                                    }
                                    notifyDataSetChanged()
                                }
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

    class FeedViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bingFeed(feed: Feed) {
            itemView.questionTxt.text = feed.questionBody
            itemView.answerTxt.text = feed.answerBody
            itemView.questionFrom.text = feed.toUserName
            itemView.answerFrom.text = feed.fromUserName
            itemView.answerDateTxt.setFormattedDateForPost(feed.answerDate)
            itemView.reactionsTxt.setTextOrHide(feed.reactionsNum.toString())

            itemView.questionUserAvatar.loadImage(feed.toUserAvatar)
            itemView.answerUserAvatar.loadImage(feed.fromUserAvatar)

            updateReactedIcon(feed.isReacted)
        }

        private fun updateReactedIcon(reaction: Reaction){
            when(reaction){
                Reaction.REACATED -> {
                    itemView.reactionsTxt.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange))
                    itemView.reactionsTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_reacted,0,0,0)
                }
                Reaction.UN_REACATED -> {
                    itemView.reactionsTxt.setTextColor(ContextCompat.getColor(itemView.context ,R.color.black))
                    itemView.reactionsTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_react,0,0,0)
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