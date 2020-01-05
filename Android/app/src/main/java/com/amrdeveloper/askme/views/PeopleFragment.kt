package com.amrdeveloper.askme.views

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.viewmodels.UserViewModel
import com.amrdeveloper.askme.adapter.PeopleAdapter
import com.amrdeveloper.askme.models.Constants
import com.amrdeveloper.askme.models.User
import com.amrdeveloper.askme.databinding.ListLayoutBinding
import com.amrdeveloper.askme.di.ViewModelProviderFactory
import com.amrdeveloper.askme.extensions.gone
import com.amrdeveloper.askme.extensions.openFragmentInto
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class PeopleFragment : DaggerFragment() {

    @Inject lateinit var mUserAdapter: PeopleAdapter
    private lateinit var mPeopleViewModel : UserViewModel
    private lateinit var mListLayoutBinding: ListLayoutBinding
    @Inject lateinit var providerFactory : ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mListLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.list_layout, container, false)

        setupUserList()

        mPeopleViewModel = ViewModelProviders.of(this, providerFactory).get(UserViewModel::class.java)

        mPeopleViewModel.loadPeopleList()

        mPeopleViewModel.getUserPagedList().observe(this, Observer {
            mUserAdapter.submitList(it)
            mListLayoutBinding.loadingBar.gone()
        })

        return mListLayoutBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchMenu = menu.findItem(R.id.searchMenu)
        val searchView : SearchView = searchMenu.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(userSearchViewListener)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupUserList() {
        mListLayoutBinding.listItems.setHasFixedSize(true)
        mListLayoutBinding.listItems.layoutManager = LinearLayoutManager(context)
        mListLayoutBinding.listItems.adapter = mUserAdapter

        mUserAdapter.setOnUserClickListener(object : PeopleAdapter.OnUserClickListener {
            override fun onClick(user: User) {
                val profileFragment = ProfileFragment()

                val args = Bundle()
                args.putString(Constants.USER_ID, user.id)
                profileFragment.arguments = args

                fragmentManager?.openFragmentInto(R.id.viewContainers, profileFragment)
            }
        })
    }

    private val userSearchViewListener = object : SearchView.OnQueryTextListener{
        override fun onQueryTextChange(newText: String?): Boolean{
            if(newText!!.isEmpty()){
                mPeopleViewModel.getUsersSearchList().removeObservers(viewLifecycleOwner)
                mUserAdapter.submitList(mPeopleViewModel.getUserPagedList().value)
            }
            return true
        }

        override fun onQueryTextSubmit(query: String?): Boolean {
            if(query.isNullOrEmpty() || query.trim().length < 3){
                Toast.makeText(context, "Invalid Query", Toast.LENGTH_SHORT).show()
                return false
            }else{
                if(mPeopleViewModel.getUsersSearchList().hasActiveObservers().not()){
                    mPeopleViewModel.getUsersSearchList().observe(viewLifecycleOwner, Observer {
                        mUserAdapter.submitList(it)
                    })
                }
                mPeopleViewModel.searchPeopleList(query)
                return true
            }
        }
    }
}