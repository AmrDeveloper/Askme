package com.amrdeveloper.askme.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.databinding.QuestionAnswerLayoutBinding
import com.amrdeveloper.askme.di.ViewModelProviderFactory
import com.amrdeveloper.askme.extensions.str
import com.amrdeveloper.askme.models.Constants
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class QuestionAnswerFragment : DaggerFragment(){


    @Inject lateinit var providerFactory : ViewModelProviderFactory
    private lateinit var mQuestionAnswerBinding : QuestionAnswerLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mQuestionAnswerBinding =
            DataBindingUtil.inflate(inflater, R.layout.question_answer_layout, container, false)

        val answerId = arguments?.getString(Constants.ANSWER_ID).str()

        return mQuestionAnswerBinding.root
    }
}