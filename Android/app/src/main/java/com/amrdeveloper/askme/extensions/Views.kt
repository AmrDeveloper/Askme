package com.amrdeveloper.askme.extensions

import android.view.View

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.hide(){
    this.visibility = View.INVISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}

fun View.clickable(){
    this.isClickable = true
}

fun View.unClickable(){
    this.isClickable = false;
}