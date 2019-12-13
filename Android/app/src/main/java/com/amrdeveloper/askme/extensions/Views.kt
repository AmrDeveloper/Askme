package com.amrdeveloper.askme.extensions

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.clickable() {
    this.isClickable = true
}

fun View.unClickable() {
    this.isClickable = false
}

fun TextView.setTextOrGone(text: String?) {
    if (text.isNullOrEmpty()) {
        this.gone()
    } else {
        this.text = text
    }
}

fun TextView.setPluralsText(id : Int, value : Int) {
    this.resources.getQuantityString(id, value,value)
}


fun ImageView.loadImage(imageUrl: String) {
    if(imageUrl.isNullOrEmpty()){
        return
    }
    val imagePath = imageUrl.replace("\\", "/")
    Picasso.get().load(imagePath).centerCrop().into(this)
}
