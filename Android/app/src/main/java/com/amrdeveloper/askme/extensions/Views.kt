package com.amrdeveloper.askme.extensions

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.net.API_SERVER_URL
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

fun TextView.setTextOrHide(text: String?) {
    if (text.isNullOrEmpty()) {
        this.hide()
    } else {
        this.text = text
    }
}

fun TextView.setPluralsText(id : Int, value : Int) {
    this.text = this.resources.getQuantityString(id, value,value)
}

fun ImageView.loadImage(imageUrl: String?) {
    if(imageUrl.isNullOrEmpty()){
        return
    }
    val imagePath = API_SERVER_URL + imageUrl.replace("\\", "/")
    Picasso.get().load(imagePath).error(R.drawable.ic_profile).into(this)
}
