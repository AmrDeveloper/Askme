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
import com.amrdeveloper.askme.data.*
import com.amrdeveloper.askme.databinding.QuestionAnswerLayoutBinding
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionAnswerFragment : Fragment(){

    private lateinit var mQuestionAnswer: Answer

    private var _binding : QuestionAnswerLayoutBinding? = null
    private val binding get() = _binding!!

    private val mQuestionAnswerViewModel by viewModels<QuestionAnswerViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.question_answer_layout, container, false)

        val token = Session.getHeaderToken(requireContext()).toString()
        val answerId = arguments?.getString(ANSWER_ID).toString()
        val userId = Session.getUserId(requireContext()).toString()

        setupObservers()

        mQuestionAnswerViewModel.getQuestionAnswer(token, answerId, userId)
        viewsClickListeners()
        return binding.root
    }

    private fun setupObservers() {
        mQuestionAnswerViewModel.getAnswerLiveData().observe(viewLifecycleOwner, {
            bindAnswer(it)
            mQuestionAnswer = it
        })
    }

    private fun bindAnswer(answer : Answer){
        binding.questionUsername.text = answer.toUserName
        binding.questionText.text = answer.questionBody
        binding.questionUserAvatar.loadImage(answer.toUserAvatar, R.drawable.ic_profile)

        binding.answerUsername.text = answer.toUserName
        binding.answerText.text = answer.answerBody
        binding.answerUserAvatar.loadImage(answer.fromUserAvatar, R.drawable.ic_profile)

        binding.reactionsTxt.text = answer.reactionsNum.toString()


        if(answer.isReacted == Reaction.REACATED){
            binding.reactionsTxt.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
            binding.reactionsTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_reacted,0,0,0)
        }else{
            binding.reactionsTxt.setTextColor(ContextCompat.getColor(requireContext() ,R.color.black))
            binding.reactionsTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_react,0,0,0)
        }
    }

    private fun viewsClickListeners(){
        binding.questionUsername.setOnClickListener {
            val bundle = bundleOf(USER_ID to mQuestionAnswer.toUserId)
            findNavController().navigate(R.id.action_questionAnswerFragment_to_peopleFragment, bundle)
        }

        binding.answerUsername.setOnClickListener{
            val bundle = bundleOf(USER_ID to mQuestionAnswer.fromUserId)
            findNavController().navigate(R.id.action_questionAnswerFragment_to_peopleFragment, bundle)
        }

        binding.reactionsTxt.setOnClickListener{
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}