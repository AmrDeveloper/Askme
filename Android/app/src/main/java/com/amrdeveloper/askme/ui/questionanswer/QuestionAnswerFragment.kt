package com.amrdeveloper.askme.ui.questionanswer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.Answer
import com.amrdeveloper.askme.data.Constants
import com.amrdeveloper.askme.data.Reaction
import com.amrdeveloper.askme.data.ReactionData
import com.amrdeveloper.askme.databinding.QuestionAnswerLayoutBinding
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionAnswerFragment : Fragment(){

    private lateinit var mQuestionAnswer: Answer
    private lateinit var mQuestionAnswerBinding : QuestionAnswerLayoutBinding

    private val mQuestionAnswerViewModel by viewModels<QuestionAnswerViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mQuestionAnswerBinding =
            DataBindingUtil.inflate(inflater, R.layout.question_answer_layout, container, false)

        val token = Session.getHeaderToken(requireContext()).toString()
        val answerId = arguments?.getString(Constants.ANSWER_ID).toString()
        val userId = Session.getUserId(requireContext()).toString()

        setupObservers()

        mQuestionAnswerViewModel.getQuestionAnswer(token, answerId, userId)
        viewsClickListeners()
        return mQuestionAnswerBinding.root
    }

    private fun setupObservers() {
        mQuestionAnswerViewModel.getAnswerLiveData().observe(viewLifecycleOwner, {
            bindAnswer(it)
            mQuestionAnswer = it
        })
    }

    private fun bindAnswer(answer : Answer){
        mQuestionAnswerBinding.questionUsername.text = answer.toUserName
        mQuestionAnswerBinding.questionText.text = answer.questionBody
        mQuestionAnswerBinding.questionUserAvatar.loadImage(answer.toUserAvatar, R.drawable.ic_profile)

        mQuestionAnswerBinding.answerUsername.text = answer.toUserName
        mQuestionAnswerBinding.answerText.text = answer.answerBody
        mQuestionAnswerBinding.answerUserAvatar.loadImage(answer.fromUserAvatar, R.drawable.ic_profile)

        mQuestionAnswerBinding.reactionsTxt.text = answer.reactionsNum.toString()


        if(answer.isReacted == Reaction.REACATED){
            mQuestionAnswerBinding.reactionsTxt.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
            mQuestionAnswerBinding.reactionsTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_reacted,0,0,0)
        }else{
            mQuestionAnswerBinding.reactionsTxt.setTextColor(ContextCompat.getColor(requireContext() ,R.color.black))
            mQuestionAnswerBinding.reactionsTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_react,0,0,0)
        }
    }

    private fun viewsClickListeners(){
        mQuestionAnswerBinding.questionUsername.setOnClickListener {
            val bundle = bundleOf(Constants.USER_ID to mQuestionAnswer.toUserId)
            findNavController().navigate(R.id.action_questionAnswerFragment_to_peopleFragment, bundle)
        }

        mQuestionAnswerBinding.answerUsername.setOnClickListener{
            val bundle = bundleOf(Constants.USER_ID to mQuestionAnswer.fromUserId)
            findNavController().navigate(R.id.action_questionAnswerFragment_to_peopleFragment, bundle)
        }

        mQuestionAnswerBinding.reactionsTxt.setOnClickListener{
            when(mQuestionAnswer.isReacted){
                Reaction.REACATED -> {
                    val token = Session.getHeaderToken(requireContext()).toString()
                    val answerId = mQuestionAnswer.answerId.toString()
                    val toUserId = mQuestionAnswer.toUserId
                    val fromUserId = mQuestionAnswer.fromUserId
                    val reactionData = ReactionData(fromUserId, toUserId, answerId)
                    mQuestionAnswerViewModel.unreactAnswer(token, reactionData)
                }

                Reaction.UN_REACATED -> {
                    val token = Session.getHeaderToken(requireContext()).toString()
                    val answerId = mQuestionAnswer.answerId.toString()
                    val toUserId = mQuestionAnswer.toUserId
                    val fromUserId = mQuestionAnswer.fromUserId
                    val reactionData = ReactionData(fromUserId, toUserId, answerId)
                    mQuestionAnswerViewModel.reactAnswer(token, reactionData)
                }
            }
        }
    }
}