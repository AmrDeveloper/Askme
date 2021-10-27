package com.amrdeveloper.askme.ui.people

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.data.Constants
import com.amrdeveloper.askme.data.User
import com.amrdeveloper.askme.databinding.ListLayoutBinding
import com.amrdeveloper.askme.extensions.gone
import com.amrdeveloper.askme.ui.adapter.PeopleAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PeopleFragment : Fragment() {

    @Inject lateinit var mUserAdapter: PeopleAdapter
    private lateinit var mListLayoutBinding: ListLayoutBinding

    private val mPeopleViewModel by viewModels<PeopleViewModel>()

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
                val bundle = bundleOf(Constants.USER_ID to user.id)
                findNavController().navigate(R.id.action_peopleFragment_to_profileFragment, bundle)
            }
        })
    }

    private val userSearchViewListener = object : SearchView.OnQueryTextListener{
        override fun onQueryTextChange(newText: String?): Boolean{
            if(newText!!.isEmpty()){
                if(view != null) mPeopleViewModel.getUsersSearchList().removeObservers(viewLifecycleOwner)
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