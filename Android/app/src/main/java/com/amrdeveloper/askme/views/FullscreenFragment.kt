package com.amrdeveloper.askme.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.databinding.FullscreenLayoutBinding
import com.amrdeveloper.askme.extensions.loadImage
import com.amrdeveloper.askme.models.Constants

class FullscreenFragment : Fragment(){

    private lateinit var mFullscreenLayoutBinding: FullscreenLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFullscreenLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.fullscreen_layout, container, false)

        val imageUrl = arguments?.getString(Constants.AVATAR_URL)
        mFullscreenLayoutBinding.imageView.loadImage(imageUrl)

        return mFullscreenLayoutBinding.root
    }
}