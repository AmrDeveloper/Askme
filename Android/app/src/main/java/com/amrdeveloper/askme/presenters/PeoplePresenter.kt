package com.amrdeveloper.askme.presenters

import androidx.lifecycle.LifecycleOwner
import com.amrdeveloper.askme.models.PeopleModel
import com.amrdeveloper.askme.models.UserViewModel
import com.amrdeveloper.askme.contracts.PeopleContract

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