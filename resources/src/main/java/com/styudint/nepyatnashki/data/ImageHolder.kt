package com.styudint.nepyatnashki.data

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData

interface ImageHolder {
    fun loadImage(uri : Uri)
    fun loadResource(resId: Int)
    fun loadFromResourceInfo(info: com.styudint.nepyatnashki.data.ResourceInfo)

    fun info(): com.styudint.nepyatnashki.data.ResourceInfo?

    fun bitmap(): LiveData<Bitmap>
}