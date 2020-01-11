package com.amrdeveloper.askme.adapter

import android.content.Context
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.NonNull
import com.amrdeveloper.askme.R
import com.amrdeveloper.askme.models.Theme
import de.hdodenhof.circleimageview.CircleImageView

class ColorGridAdapter(@NonNull context: Context, themeList: List<Theme>) :
    ArrayAdapter<Theme>(context, 0, themeList) {

    @NonNull
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.color_list_item, parent, false)
        }
        val colorCircleView : CircleImageView = view!!.findViewById(R.id.colorImageView)
        val theme = getItem(position)
        colorCircleView.setImageResource(theme!!.colorValue)
        return view
    }
}