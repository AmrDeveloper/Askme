package com.amrdeveloper.askme.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.askme.contracts.PeopleContract
import com.amrdeveloper.askme.presenters.PeoplePresenter
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.models.UserViewModel
import com.amrdeveloper.askme.adapter.PeopleAdapter
import com.amrdeveloper.askme.data.Constants
import com.amrdeveloper.askme.data.User
import com.amrdeveloper.askme.extensions.gone
import com.amrdeveloper.askme.extensions.openFragmentInto
import com.amrdeveloper.askme.extensions.show

class PeopleFragment : Fragment(), PeopleContract.View {

    private lateinit var mLoadingBar: ProgressBar
    private lateinit var mUserAdapter: PeopleAdapter
    private lateinit var mPeoplePresenter: PeoplePresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_layout, container, false)
        mLoadingBar = view.findViewById(R.id.loadingBar)
        setupUserList(view)

        val userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.getUserPagedList().observe(this, Observer{
            mUserAdapter.submitList(it)
            hideProgressBar()
        })

        mPeoplePresenter = PeoplePresenter(this, userViewModel, this)

        mPeoplePresenter.startLoadingPeople()

        return view
    }

    private fun setupUserList(view: View) {
        mUserAdapter = PeopleAdapter()
        val listItems = view.findViewById<RecyclerView>(R.id.listItems)
        listItems.setHasFixedSize(true)
        listItems.layoutManager = LinearLayoutManager(context)
        listItems.adapter = mUserAdapter

        mUserAdapter.setOnUserClickListener(object : PeopleAdapter.OnUserClickListener {
            override fun onClick(user: User) {
                val profileFragment = ProfileFragment()

                val args = Bundle()
                args.putString(Constants.USER_ID, user.id)
                args.putString(Constants.EMAIL, user.email)
                profileFragment.arguments = args

                fragmentManager?.openFragmentInto(R.id.viewContainers, profileFragment)
            }
        })
    }


    override fun showProgressBar() {
        mLoadingBar.show()
    }

    override fun hideProgressBar() {
        mLoadingBar.gone()
    }
}