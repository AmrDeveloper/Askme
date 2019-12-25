package com.amrdeveloper.askme.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.Constants
import com.amrdeveloper.askme.databinding.AskQuestionViewBinding
import com.amrdeveloper.askme.extensions.loadImage
import com.amrdeveloper.askme.extensions.str
import com.amrdeveloper.askme.utils.Session

class AskQuestionFragment : Fragment(){

    private lateinit var mAskQuestionViewBinding: AskQuestionViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAskQuestionViewBinding =
            DataBindingUtil.inflate(inflater, R.layout.ask_question_view, container, false)

        bindUserInformation()
        updateQuestionLength()

        return mAskQuestionViewBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.send_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sendMenu){
            val question = mAskQuestionViewBinding.questionEditText.text.str()
            val isAnonymously = mAskQuestionViewBinding.anonymouslySwitch.isChecked
            val fromUser = Session().getUserId(context!!).str()
            val userId = arguments?.getString(Constants.USER_ID)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindUserInformation(){
        val name = arguments?.getString(Constants.NAME)
        val username = arguments?.getString(Constants.USERNAME)
        val avatarUrl = arguments?.getString(Constants.AVATAR_URL)

        mAskQuestionViewBinding.userName.text = name
        mAskQuestionViewBinding.userUsername.text = username
        mAskQuestionViewBinding.userAvatar.loadImage(avatarUrl)
    }

    private fun updateQuestionLength(){
        mAskQuestionViewBinding.questionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable : Editable?) {
                mAskQuestionViewBinding.questionLength.text = (300 - editable!!.length).str()
            }
        })
    }
}