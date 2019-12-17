package com.amrdeveloper.askme.contracts

import androidx.lifecycle.LifecycleOwner
import com.amrdeveloper.askme.HomeViewModel

interface HomeContract {

    interface Model{
        fun loadHomeFeedFromServer(viewModel: HomeViewModel, lifecycleOwner: LifecycleOwner)
    }

    interface View{
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter{
        fun startLoadingHomeFeed()
    }
}