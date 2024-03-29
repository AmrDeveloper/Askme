package com.amrdeveloper.askme.ui.askquestion

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.*
import com.amrdeveloper.askme.data.ResponseType
import com.amrdeveloper.askme.databinding.AskQuestionLayoutBinding
import com.amrdeveloper.askme.utils.Session
import com.amrdeveloper.askme.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AskQuestionFragment : Fragment(){

    private var _binding: AskQuestionLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<QuestionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.ask_question_layout, container, false)

        setupObservers()
        bindUserInformation()
        updateQuestionLength()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.send_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sendMenu){
            val question = binding.questionEditText.text.toString()
            val isAnonymously = binding.anonymouslySwitch.isChecked
            var isAnonymous = "0"
            if(isAnonymously) isAnonymous = "1"
            val fromUser = Session.getUserId(requireContext()).toString()
            val toUser = arguments?.getString(USER_ID).toString()
            val questionData = QuestionData(question,toUser, fromUser, isAnonymous)
            val token = Session.getUserToken(requireContext()).toString()
            viewModel.askNewQuestion(token, questionData)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupObservers() {
        viewModel.getQuestionLiveData().observe(viewLifecycleOwner, {
            if (it == ResponseType.SUCCESS) findNavController().navigateUp()
            else if (it == ResponseType.FAILURE) Log.d("QUESTION","Invalid")
        })
    }

    private fun bindUserInformation(){
        val name = arguments?.getString(NAME)
        val username = arguments?.getString(USERNAME)
        val avatarUrl = arguments?.getString(AVATAR_URL)

        binding.userName.text = name
        binding.userUsername.text = username
        binding.userAvatar.loadImage(avatarUrl, R.drawable.ic_profile)
    }

    private fun updateQuestionLength(){
        binding.questionEditText.addTextChangedListener(object : TextWatcher {
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