package com.amrdeveloper.askme.views

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.databinding.FullscreenLayoutBinding
import com.amrdeveloper.askme.extensions.loadImage
import com.amrdeveloper.askme.extensions.toServerImageUrl
import com.amrdeveloper.askme.models.Constants

class FullscreenFragment : Fragment(){

    private lateinit var mFullscreenLayoutBinding: FullscreenLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.image_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.saveMenu -> saveImageAction()
            R.id.shareMenu -> shareImageAction()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveImageAction(){
        //val imageUrl = arguments?.getString(Constants.AVATAR_URL)?.toServerImageUrl()
        //TODO : Create Image Download in Storage Later after find the best way
    }

    private fun shareImageAction(){
        val imageUrl = arguments?.getString(Constants.AVATAR_URL)?.toServerImageUrl()
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Image")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, imageUrl)
        startActivity(Intent.createChooser(sharingIntent,resources.getString(R.string.share)))
    }
}