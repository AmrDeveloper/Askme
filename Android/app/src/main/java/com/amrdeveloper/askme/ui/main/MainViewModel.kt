package com.amrdeveloper.askme.ui.main

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _navigationBottomVisibility = MutableLiveData(View.GONE)
    val navigationBottomVisibility = _navigationBottomVisibility

    fun updateNavigationButtonVisibility(visibility : Int) {
        navigationBottomVisibility.value = visibility
    }
}