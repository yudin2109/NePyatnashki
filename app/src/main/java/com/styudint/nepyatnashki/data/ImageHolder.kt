package com.styudint.nepyatnashki.data

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData

interface ImageHolder {
    fun loadImage(uri : Uri)

    fun bitmap(): LiveData<Bitmap>
}