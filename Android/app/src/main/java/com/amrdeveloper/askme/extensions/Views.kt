package com.amrdeveloper.askme.extensions

import android.view.View
import android.widget.ImageView
import android.widget.TextView

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
    this.isClickable = false
}

fun TextView.setTextOrGone(text : String?){
    if(text.isNullOrEmpty()){
        this.gone()
    }else{
        this.text = text
    }
}

fun ImageView.loadImage(imageUrl : String){

}