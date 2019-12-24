package com.amrdeveloper.askme.views

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
import com.amrdeveloper.askme.extensions.notNull
import com.amrdeveloper.askme.extensions.openFragmentInto
import com.amrdeveloper.askme.extensions.show
import com.amrdeveloper.askme.net.AskmeClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeopleFragment : Fragment(), PeopleContract.View {

    private lateinit var mLoadingBar: ProgressBar
    private lateinit var mUserAdapter: PeopleAdapter
    private lateinit var mPeoplePresenter: PeoplePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_layout, container, false)
        mLoadingBar = view.findViewById(R.id.loadingBar)
        setupUserList(view)

        val userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.getUserPagedList().observe(this, Observer {
            mUserAdapter.submitList(it)
            hideProgressBar()
        })

        mPeoplePresenter = PeoplePresenter(this, userViewModel, this)

        mPeoplePresenter.startLoadingPeople()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchMenu = menu.findItem(R.id.searchMenu)
        val searchView : SearchView = searchMenu.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(userSearchViewListener)

        super.onCreateOptionsMenu(menu, inflater)
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

    private val userSearchViewListener = object : SearchView.OnQueryTextListener{
        override fun onQueryTextChange(newText: String?): Boolean = true

        override fun onQueryTextSubmit(query: String?): Boolean {
            if(query.isNullOrEmpty() || query.trim().length < 3){
                Toast.makeText(context, "Invalid Query", Toast.LENGTH_SHORT).show()
                return false
            }else{
                /*
                val keyword = query.trim()
                AskmeClient.getUserService().getUsersSearch(keyword)
                    .enqueue(object : Callback<List<User>>{
                        override fun onResponse(
                            call: Call<List<User>>,
                            response: Response<List<User>>
                        ) {
                            if(response.code() == 200){
                                val usersList = response.body()
                                usersList.notNull {

                                }
                            }
                        }

                        override fun onFailure(call: Call<List<User>>, t: Throwable) {
                            Toast.makeText(context, "Invalid Search", Toast.LENGTH_SHORT).show()
                        }
                    })
                 */
            }
            return true
        }
    }
}