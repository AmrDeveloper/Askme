package com.amrdeveloper.askme.extensions

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.net.API_SERVER_URL
import com.amrdeveloper.askme.utils.formatDateForJoin
import com.amrdeveloper.askme.utils.formatDateForPost
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

fun TextView.setFormattedJoinDate(time : Long) {
    this.text = formatDateForJoin(time).str()
}

fun TextView.setFormattedDateForPost(time : Long) {
    this.text = formatDateForPost(time).str()
}

fun TextView.setPluralsText(id : Int, value : Int) {
    this.text = this.resources.getQuantityString(id, value,value)
}

fun ImageView.loadImage(imageUrl: String?) {
    if(imageUrl.isNullOrEmpty()){
        Picasso.get().load(R.drawable.ic_profile).error(R.drawable.ic_profile).into(this)
        return
    }
    val imagePath = API_SERVER_URL + imageUrl.replace("\\", "/")
    Picasso.get().load(imagePath).error(R.drawable.ic_profile).into(this)
}

fun View.backgroundColor(context : Context, @ColorRes colorId : Int){
    this.setBackgroundColor(ContextCompat.getColor(context, colorId))
}

fun CardView.backgroundColor(context : Context, @ColorRes colorId : Int){
    this.setCardBackgroundColor(ContextCompat.getColor(context, colorId))
}
