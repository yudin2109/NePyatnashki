package com.styudint.nepyatnashki.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.styudint.nepyatnashki.R
import com.styudint.nepyatnashki.data.ResourceInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Classic15GalleryAdapter(private val ctx: Context) : GalleryImageAdapter {
    override fun amountOfImages() = 1

    override fun getBitmap(index: Int): LiveData<Bitmap> {
        val liveData = MutableLiveData<Bitmap>()
        GlobalScope.launch {
            liveData.postValue(BitmapFactory.decodeResource(ctx.resources, R.drawable.classic15))
        }
        return liveData
    }

    override fun getResourceInfo(index: Int): ResourceInfo {
        return ResourceInfo.fromResource(R.drawable.classic15)
    }

    override fun getName(): String = "Classic"
}