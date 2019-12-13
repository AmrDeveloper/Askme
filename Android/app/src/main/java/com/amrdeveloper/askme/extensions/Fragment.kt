package com.amrdeveloper.askme.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.openFragmentInto(container : Int, fragment: Fragment){
    this.beginTransaction()
        .replace(container, fragment)
        .addToBackStack(fragment.javaClass.name)
        .commit()
}