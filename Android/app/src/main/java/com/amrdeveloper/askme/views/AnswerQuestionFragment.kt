package com.amrdeveloper.askme.views

import android.os.Bundle
import com.amrdeveloper.askme.utils.Session
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.models.AnswerData
import com.amrdeveloper.askme.models.Constants
import com.amrdeveloper.askme.models.Question
import com.amrdeveloper.askme.databinding.AnswerQuestionLayoutBinding
import com.amrdeveloper.askme.extensions.loadImage
import com.amrdeveloper.askme.extensions.notNull
import com.amrdeveloper.askme.extensions.str
import com.amrdeveloper.askme.net.AskmeClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnswerQuestionFragment : Fragment() {

    private lateinit var mQuestion: Question
    private lateinit var mAnswerQuestionLayoutBinding: AnswerQuestionLayoutBinding

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
        getQuestionInformation()
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
            val fromUserId = Session().getUserId(context!!).str()
            val toUserId = mQuestion.fromUserId
            val answerData = AnswerData(questionID,answerBody,fromUserId, toUserId)
            answerOneQuestion(answerData)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getQuestionInformation(){
        val questionID = arguments?.getString(Constants.QUESTION_ID).str()
        AskmeClient.getQuestionService().getQuestionById(
            token = Session().getHeaderToken(context!!).str(),
            questionId = questionID
        ).enqueue(object : Callback<Question>{
            override fun onResponse(call: Call<Question>, response: Response<Question>) {
                if(response.code() == 200){
                    response.body().notNull {
                        bindQuestionInformation(it)
                        mQuestion = it
                    }
                }else{
                    Log.d("Answer","Invalid Answer Request ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Question>, t: Throwable) {
                Log.d("Answer","Invalid Answer Request")
            }
        })
    }

    private fun bindQuestionInformation(question : Question){
        mAnswerQuestionLayoutBinding.questionText.text = question.title
        mAnswerQuestionLayoutBinding.userUsername.text = question.fromUserName
        mAnswerQuestionLayoutBinding.userAvatar.loadImage(question.fromUserAvatar)
    }

    private fun answerOneQuestion(answerData: AnswerData){
        AskmeClient.getAnswerService().answerOneQuestion(
            token = Session().getHeaderToken(context!!).str(),
            answerData = answerData
        ).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.code() == 200){
                    fragmentManager?.popBackStackImmediate()
                }else{
                    Log.d("Answer","Invalid Answer Request ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("Answer","Invalid Answer Request ${t.message}")
            }
        })
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