package com.amrdeveloper.askme.views

import android.os.Bundle
import com.amrdeveloper.askme.utils.Session
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.models.AnswerData
import com.amrdeveloper.askme.models.Constants
import com.amrdeveloper.askme.models.Question
import com.amrdeveloper.askme.databinding.AnswerQuestionLayoutBinding
import com.amrdeveloper.askme.extensions.loadImage
import com.amrdeveloper.askme.extensions.str
import com.amrdeveloper.askme.models.Anonymously
import com.amrdeveloper.askme.net.ResponseType
import com.amrdeveloper.askme.viewmodels.AnswerQuestionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnswerQuestionFragment : Fragment() {

    private lateinit var mQuestion: Question
    private lateinit var mAnswerQuestionLayoutBinding: AnswerQuestionLayoutBinding

    private val mAnswerQuestionViewModel by viewModels <AnswerQuestionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAnswerQuestionLayoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.answer_question_layout, container, false)

        val token = Session.getHeaderToken(context!!).str()
        val questionID = arguments?.getString(Constants.QUESTION_ID).str()
        mAnswerQuestionViewModel.getQuestionById(token, questionID)

        mAnswerQuestionViewModel.getQuestinLiveData().observe(this, Observer {
            bindQuestionInformation(it)
            mQuestion = it
        })

        mAnswerQuestionViewModel.getAnswerLiveData().observe(this, Observer {
            when(it){
                ResponseType.SUCCESS ->  fragmentManager?.popBackStackImmediate()
                ResponseType.FAILURE -> Toast.makeText(context, "Invalid Answer Request", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(context, "Invalid Answer Request", Toast.LENGTH_SHORT).show()
            }
        })

        updateQuestionLength()
        return mAnswerQuestionLayoutBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.send_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sendMenu){
            val questionID = arguments?.getString(Constants.QUESTION_ID).str()
            val answerBody = mAnswerQuestionLayoutBinding.answerEditText.text.str()
            val fromUserId = Session.getUserId(context!!).str()
            val toUserId = mQuestion.fromUserId
            val answerData = AnswerData(questionID,answerBody,fromUserId, toUserId)
            val token = Session.getHeaderToken(context!!).str()
            mAnswerQuestionViewModel.answerQuestion(token, answerData)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindQuestionInformation(question : Question){
        mAnswerQuestionLayoutBinding.questionText.text = question.title

        if(question.anonymously == Anonymously.ANONYMOSLY){
            mAnswerQuestionLayoutBinding.userUsername.text = getString(R.string.anonymous_user)
        }else{
            mAnswerQuestionLayoutBinding.userUsername.text = question.fromUserName
            mAnswerQuestionLayoutBinding.userAvatar.loadImage(question.fromUserAvatar, R.drawable.ic_profile)
        }
    }

    private fun updateQuestionLength(){
        mAnswerQuestionLayoutBinding.answerEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable : Editable?) {
                mAnswerQuestionLayoutBinding.questionLength.text = (300 - editable!!.length).str()
            }
        })
    }
}