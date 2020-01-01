package com.amrdeveloper.askme.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.models.Constants
import com.amrdeveloper.askme.models.QuestionData
import com.amrdeveloper.askme.databinding.AskQuestionLayoutBinding
import com.amrdeveloper.askme.extensions.loadImage
import com.amrdeveloper.askme.extensions.str
import com.amrdeveloper.askme.net.ResponseType
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.viewmodels.QuestionViewModel

class AskQuestionFragment : Fragment(){

    private lateinit var mQuestionViewModel : QuestionViewModel
    private lateinit var mAskQuestionLayoutBinding: AskQuestionLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAskQuestionLayoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.ask_question_layout, container, false)
        mQuestionViewModel = ViewModelProviders.of(this).get(QuestionViewModel::class.java)

        mQuestionViewModel.getQuestionLiveData().observe(this, Observer {
            when(it){
                ResponseType.SUCCESS -> {
                    fragmentManager?.popBackStackImmediate()
                }
                ResponseType.FAILURE -> {
                    Log.d("QUESTION","Invalid")
                }
            }
        })

        bindUserInformation()
        updateQuestionLength()

        return mAskQuestionLayoutBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.send_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sendMenu){
            val question = mAskQuestionLayoutBinding.questionEditText.text.str()
            val isAnonymously = mAskQuestionLayoutBinding.anonymouslySwitch.isChecked.str()
            val fromUser = Session.getUserId(context!!).str()
            val toUser = arguments?.getString(Constants.USER_ID).str()
            val questionData = QuestionData(question,fromUser, toUser, isAnonymously)
            val token = Session.getUserToken(context!!).str()
            mQuestionViewModel.askNewQuestion(token, questionData)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindUserInformation(){
        val name = arguments?.getString(Constants.NAME)
        val username = arguments?.getString(Constants.USERNAME)
        val avatarUrl = arguments?.getString(Constants.AVATAR_URL)

        mAskQuestionLayoutBinding.userName.text = name
        mAskQuestionLayoutBinding.userUsername.text = username
        mAskQuestionLayoutBinding.userAvatar.loadImage(avatarUrl)
    }

    private fun updateQuestionLength(){
        mAskQuestionLayoutBinding.questionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable : Editable?) {
                mAskQuestionLayoutBinding.questionLength.text = (300 - editable!!.length).str()
            }
        })
    }
}