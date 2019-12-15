package com.amrdeveloper.askme

import androidx.lifecycle.LifecycleOwner

class PeoplePresenter(
    private val view : PeopleContract.View,
    private val userViewModel: UserViewModel,
    private val owner: LifecycleOwner
) : PeopleContract.Presenter{

    private val model : PeopleContract.Model

    init {
        model = PeopleModel()
    }

    override fun startLoadingPeople() {
        view.showProgressBar()
        model.loadPeopleFromServer(userViewModel, owner)
    }

}