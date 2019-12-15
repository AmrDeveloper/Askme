package com.amrdeveloper.askme

import androidx.lifecycle.LifecycleOwner

interface PeopleContract {

    interface Model{
        fun loadPeopleFromServer(userViewModel: UserViewModel, lifecycleOwner: LifecycleOwner)
    }

    interface View{
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter{
        fun startLoadingPeople()
    }
}