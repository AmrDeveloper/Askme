package com.amrdeveloper.askme.extensions

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
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

fun ImageView.loadImage(imageUrl: String?, defaultDrawableId : Int) {
    if(imageUrl.isNullOrEmpty()) return
    val imagePath = API_SERVER_URL + imageUrl.replace("\\", "/")
    Picasso.get()
        .load(imagePath)
        .error(defaultDrawableId).into(this)
}

fun CardView.backgroundColor(context : Context, @ColorRes colorId : Int){
    this.setCardBackgroundColor(ContextCompat.getColor(context, colorId))
}
