package com.amrdeveloper.askme.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.databinding.QuestionAnswerLayoutBinding
import com.amrdeveloper.askme.extensions.loadImage
import com.amrdeveloper.askme.extensions.openFragmentInto
import com.amrdeveloper.askme.extensions.str
import com.amrdeveloper.askme.models.Answer
import com.amrdeveloper.askme.models.Constants
import com.amrdeveloper.askme.models.Reaction
import com.amrdeveloper.askme.models.ReactionData
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.viewmodels.QuestionAnswerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionAnswerFragment : Fragment(){

    private lateinit var mQuestionAnswer: Answer
    private lateinit var mQuestionAnswerBinding : QuestionAnswerLayoutBinding

    private val mQuestionAnswerViewModel by viewModels<QuestionAnswerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mQuestionAnswerBinding =
            DataBindingUtil.inflate(inflater, R.layout.question_answer_layout, container, false)

        val token = Session.getHeaderToken(context!!).str()
        val answerId = arguments?.getString(Constants.ANSWER_ID).str()
        val userId = Session.getUserId(context!!).str()

        mQuestionAnswerViewModel.getAnswerLiveData().observe(this, Observer {
            bindAnswer(it)
            mQuestionAnswer = it
        })

        mQuestionAnswerViewModel.getQuestionAnswer(token, answerId, userId)
        viewsClickListeners()
        return mQuestionAnswerBinding.root
    }

    private fun bindAnswer(answer : Answer){
        mQuestionAnswerBinding.questionUsername.text = answer.toUserName
        mQuestionAnswerBinding.questionText.text = answer.questionBody
        mQuestionAnswerBinding.questionUserAvatar.loadImage(answer.toUserAvatar, R.drawable.ic_profile)

        mQuestionAnswerBinding.answerUsername.text = answer.toUserName
        mQuestionAnswerBinding.answerText.text = answer.answerBody
        mQuestionAnswerBinding.answerUserAvatar.loadImage(answer.fromUserAvatar, R.drawable.ic_profile)

        mQuestionAnswerBinding.reactionsTxt.text = answer.reactionsNum.str()

        if(answer.isReacted == Reaction.REACATED){
            mQuestionAnswerBinding.reactionsTxt.setTextColor(ContextCompat.getColor(context!!, R.color.orange))
            mQuestionAnswerBinding.reactionsTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_reacted,0,0,0)
        }else{
            mQuestionAnswerBinding.reactionsTxt.setTextColor(ContextCompat.getColor(context!! ,R.color.black))
            mQuestionAnswerBinding.reactionsTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_react,0,0,0)
        }
    }

    private fun viewsClickListeners(){
        mQuestionAnswerBinding.questionUsername.setOnClickListener {
            val profileFragment = ProfileFragment()

            val args = Bundle()
            args.putString(Constants.USER_ID, mQuestionAnswer.toUserId)
            profileFragment.arguments = args

            fragmentManager?.openFragmentInto(R.id.viewContainers, profileFragment)
        }

        mQuestionAnswerBinding.answerUsername.setOnClickListener{
            val profileFragment = ProfileFragment()

            val args = Bundle()
            args.putString(Constants.USER_ID,  mQuestionAnswer.fromUserId)
            profileFragment.arguments = args

            fragmentManager?.openFragmentInto(R.id.viewContainers, profileFragment)
        }

        mQuestionAnswerBinding.reactionsTxt.setOnClickListener{
            when(mQuestionAnswer.isReacted){
                Reaction.REACATED -> {
                    val token = Session.getHeaderToken(context!!).str()
                    val answerId = mQuestionAnswer.answerId.str()
                    val toUserId = mQuestionAnswer.toUserId
                    val fromUserId = mQuestionAnswer.fromUserId
                    val reactionData = ReactionData(fromUserId, toUserId, answerId)
                    mQuestionAnswerViewModel.unreactAnswer(token, reactionData)
                }

                Reaction.UN_REACATED -> {
                    val token = Session.getHeaderToken(context!!).str()
                    val answerId = mQuestionAnswer.answerId.str()
                    val toUserId = mQuestionAnswer.toUserId
                    val fromUserId = mQuestionAnswer.fromUserId
                    val reactionData = ReactionData(fromUserId, toUserId, answerId)
                    mQuestionAnswerViewModel.reactAnswer(token, reactionData)
                }
            }
        }
    }
}