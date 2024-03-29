package com.amrdeveloper.askme.ui.answerquestion

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.Anonymously
import com.amrdeveloper.askme.data.AnswerData
import com.amrdeveloper.askme.data.QUESTION_ID
import com.amrdeveloper.askme.data.Question
import com.amrdeveloper.askme.data.ResponseType
import com.amrdeveloper.askme.databinding.AnswerQuestionLayoutBinding
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnswerQuestionFragment : Fragment() {

    private lateinit var mQuestion: Question
    private var _binding: AnswerQuestionLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels <AnswerQuestionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.answer_question_layout, container, false)

        setupObservers()

        val token = Session.getHeaderToken(requireContext())
        val questionID = arguments?.getString(QUESTION_ID).toString()
        viewModel.getQuestionById(token, questionID)

        updateQuestionLength()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.send_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sendMenu){
            val questionID = arguments?.getString(QUESTION_ID).toString()
            val answerBody = binding.answerEditText.text.toString()
            val fromUserId = Session.getUserId(requireContext()).toString()
            val toUserId = mQuestion.fromUserId
            val answerData = AnswerData(questionID,answerBody,fromUserId, toUserId)
            val token = Session.getHeaderToken(requireContext())
            viewModel.answerQuestion(token, answerData)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupObservers() {
        viewModel.getQuestionLiveData().observe(viewLifecycleOwner, {
            bindQuestionInformation(it)
            mQuestion = it
        })

        viewModel.getAnswerLiveData().observe(viewLifecycleOwner, {
            when(it){
                ResponseType.SUCCESS ->  findNavController().navigateUp()
                ResponseType.FAILURE -> Toast.makeText(context, "Invalid Answer Request", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(context, "Invalid Answer Request", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun bindQuestionInformation(question : Question){
        binding.questionText.text = question.title

        if(question.anonymously == Anonymously.ANONYMOSLY){
            binding.userUsername.text = getString(R.string.anonymous_user)
        }else{
            binding.userUsername.text = question.fromUserName
            binding.userAvatar.loadImage(question.fromUserAvatar, R.drawable.ic_profile)
        }
    }

    private fun updateQuestionLength(){
        binding.answerEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable : Editable?) {
                binding.questionLength.text = (300 - editable!!.length).toString()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}