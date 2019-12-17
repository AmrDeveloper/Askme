package com.amrdeveloper.askme

import androidx.lifecycle.LifecycleOwner
import com.amrdeveloper.askme.contracts.HomeContract

class HomePresenter(
    private val view: HomeContract.View,
    private val viewModel: HomeViewModel,
    private val owner: LifecycleOwner
) : HomeContract.Presenter {

    private val model: HomeContract.Model

    init {
        model = HomeModel()
    }

    override fun startLoadingHomeFeed() {
        view.showProgressBar()
        model.loadHomeFeedFromServer(viewModel, owner)
    }
}