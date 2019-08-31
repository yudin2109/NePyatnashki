package com.styudint.nepyatnashki.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.SparseArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.styudint.nepyatnashki.R
import com.styudint.nepyatnashki.data.ResourceInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LandscapeGalleryAdapter(private val ctx: Context) : GalleryImageAdapter {
    private val resources = arrayOf(
        R.drawable.landscape_1,
        R.drawable.landscape_2,
        R.drawable.landscape_3,
        R.drawable.landscape_4,
        R.drawable.landscape_5,
        R.drawable.landscape_6
    )

    private val cache = SparseArray<LiveData<Bitmap>>()

    override fun amountOfImages(): Int = resources.size

    override fun getBitmap(index: Int): LiveData<Bitmap> {
        if (cache.get(index) != null)
            return cache.get(index)

        val liveData = MutableLiveData<Bitmap>()

        GlobalScope.launch {
            val options = BitmapFactory.Options()
            options.inSampleSize = 6 // Quick hack should be rewritten to create nice image
            liveData.postValue(BitmapFactory.decodeResource(ctx.resources, resources[index], options))
        }

        cache.put(index, liveData)
        return cache[index]
    }

    override fun getResourceInfo(index: Int): ResourceInfo {
        return ResourceInfo.fromResource(resources[index])
    }

    override fun getName(): String = "Landscapes"
}