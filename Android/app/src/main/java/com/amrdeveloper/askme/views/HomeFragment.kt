package com.amrdeveloper.askme.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.adapter.FeedAdapter

class HomeFragment : Fragment() {

    private lateinit var mLoadingBar: ProgressBar
    private lateinit var mFeedAdapter : FeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_layout, container, false)
        mLoadingBar = view.findViewById(R.id.loadingBar)

        return view
    }
}