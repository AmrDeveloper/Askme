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
import com.amrdeveloper.askme.ui.adapter.PeopleAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeopleFragment : Fragment() {

    private lateinit var peopleAdapter: PeopleAdapter

    private lateinit var binding: ListLayoutBinding

    private val viewModel by viewModels<PeopleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.list_layout, container, false)

        setupUserList()

        viewModel.loadPeopleList()

        setupObservers()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchMenu = menu.findItem(R.id.searchMenu)
        val searchView : SearchView = searchMenu.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(userSearchViewListener)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupObservers() {
        viewModel.getUserPagedList().observe(viewLifecycleOwner, {
            peopleAdapter.submitList(it)
        })
    }

    private fun setupUserList() {
        peopleAdapter = PeopleAdapter()
        binding.listItems.setHasFixedSize(true)
        binding.listItems.layoutManager = LinearLayoutManager(context)
        binding.listItems.adapter = peopleAdapter

        peopleAdapter.setOnUserClickListener { user ->
            val bundle = bundleOf(Constants.USER_ID to user.id)
            findNavController().navigate(R.id.action_peopleFragment_to_profileFragment, bundle)
        }
    }

    private val userSearchViewListener = object : SearchView.OnQueryTextListener{
        override fun onQueryTextChange(newText: String?): Boolean{
            newText?.let {
                if (it.isEmpty()) return@let
                if(view != null) viewModel.getUsersSearchList().removeObservers(viewLifecycleOwner)
                peopleAdapter.submitList(viewModel.getUserPagedList().value)
            }
            return true
        }

        override fun onQueryTextSubmit(query: String?): Boolean {
            return if(query.isNullOrEmpty() || query.trim().length < 3){
                Toast.makeText(context, "Invalid Query", Toast.LENGTH_SHORT).show()
                false
            } else{
                if(viewModel.getUsersSearchList().hasActiveObservers().not()){
                    viewModel.getUsersSearchList().observe(viewLifecycleOwner, {
                        peopleAdapter.submitList(it)
                    })
                }
                viewModel.searchPeopleList(query)
                true
            }
        }
    }
}