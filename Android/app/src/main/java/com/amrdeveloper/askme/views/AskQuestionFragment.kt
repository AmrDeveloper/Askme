package com.amrdeveloper.askme.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.models.Constants
import com.amrdeveloper.askme.models.QuestionData
import com.amrdeveloper.askme.databinding.AskQuestionLayoutBinding
import com.amrdeveloper.askme.extensions.loadImage
import com.amrdeveloper.askme.extensions.str
import com.amrdeveloper.askme.net.AskmeClient
import com.amrdeveloper.askme.utils.Session
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AskQuestionFragment : Fragment(){

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
            askNewQuestion(questionData)

        }
        return super.onOptionsItemSelected(item)
    }

    private fun askNewQuestion(questionData: QuestionData){
        AskmeClient.getQuestionService().createNewQueestion(
            token = Session.getUserToken(context!!).str(),
            question = questionData
        ).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.code() == 200) {
                    fragmentManager?.popBackStackImmediate()
                }else{
                    Log.d("QUESTION","Invalid ${response.code()}")

                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("QUESTION","Invalid")
            }
        })
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