package com.amrdeveloper.askme.contracts

import androidx.lifecycle.LifecycleOwner
import com.amrdeveloper.askme.models.UserViewModel

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