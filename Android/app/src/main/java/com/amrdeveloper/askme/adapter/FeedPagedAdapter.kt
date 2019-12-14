package com.amrdeveloper.askme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.Feed
import com.amrdeveloper.askme.extensions.notNull
import kotlinx.android.synthetic.main.feed_list_item.view.*

class FeedPagedAdapter : PagedListAdapter<Feed, FeedPagedAdapter.FeedViewHolder>(DIFF_CALL_BACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.feed_list_item, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val currentFeed : Feed? = getItem(position)
        currentFeed.notNull {
            holder.bingFeed(currentFeed!!)
        }
    }

    class FeedViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bingFeed(feed : Feed){
            itemView.questionTxt.text = feed.questionBody
            itemView.answerTxt.text = feed.answerBody
            itemView.reactionsTxt.text = feed.reactionsNum.toString()
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