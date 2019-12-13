package com.amrdeveloper.askme.extensions

import android.content.res.Resources

fun Resources.plurals(id : Int, value : Int) : String{
    return this.getQuantityString(id, value,value)
}