package com.amrdeveloper.askme.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.extensions.openFragmentInto
import com.amrdeveloper.askme.utils.Session
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (Session.isUserLogined(requireContext())) fragmentManager?.openFragmentInto(R.id.viewContainers, HomeFragment())
        else  fragmentManager?.openFragmentInto(R.id.viewContainers, LoginFragment())
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}