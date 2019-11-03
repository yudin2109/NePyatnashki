package com.styudint.nepyatnashki.adapters

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.styudint.nepyatnashki.data.ResourceInfo

interface GalleryImageAdapter {
    fun amountOfImages(): Int
    fun getBitmap(index: Int): LiveData<Bitmap>
    fun getSizes(index: Int): Pair<Int, Int>
    fun getName(): String
    fun getResourceInfo(index: Int): ResourceInfo
}