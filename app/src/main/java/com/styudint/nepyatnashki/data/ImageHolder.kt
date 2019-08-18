package com.styudint.nepyatnashki.data

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData

interface ImageHolder {
    fun loadImage(uri : Uri)
    fun loadResource(resId: Int)
    fun loadFromResourceInfo(info: ResourceInfo)

    fun info(): ResourceInfo?

    fun bitmap(): LiveData<Bitmap>
}