package com.amrdeveloper.askme.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

object FileUtils {

    fun getImagePath(context: Context, uri: Uri?): String {
        var result: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        val cursor =
            context.contentResolver.query(uri!!, projection, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(projection[0])
                result = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result.toString()
    }

    fun getFileType(context: Context, uri : Uri) : String{
        return context.contentResolver.getType(uri).toString()
    }
}