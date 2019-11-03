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

class MishaGalleryAdapter(private val ctx: Context) : GalleryImageAdapter {
    private val resources = arrayOf(
            R.drawable.test_misha_0,
            R.drawable.test_misha_1,
            R.drawable.test_misha_2,
            R.drawable.test_misha_3,
            R.drawable.test_misha_4,
            R.drawable.test_misha_5,
            R.drawable.test_misha_6,
            R.drawable.test_misha_7
    )

    private val sizeList = arrayOf(
            Pair(3, 3),
            Pair(3, 3),
            Pair(4, 4),
            Pair(4, 4),
            Pair(5, 5),
            Pair(5, 5),
            Pair(6, 6),
            Pair(6, 6)
    )


    private val cache = SparseArray<LiveData<Bitmap>>()

    override fun amountOfImages(): Int = resources.size

    override fun getBitmap(index: Int): LiveData<Bitmap> {
        if (cache.get(index) != null)
            return cache.get(index)

        val liveData = MutableLiveData<Bitmap>()

        GlobalScope.launch {
            val options = BitmapFactory.Options()
            options.inSampleSize = 2 // Quick hack should be rewritten to create nice image
            liveData.postValue(BitmapFactory.decodeResource(ctx.resources, resources[index], options))
        }

        cache.put(index, liveData)
        return cache[index]
    }

    override fun getResourceInfo(index: Int): ResourceInfo {
        return ResourceInfo.fromResource(resources[index])
    }

    override fun getSizes(index: Int): Pair<Int, Int> {
        return sizeList[index]
    }

    override fun getName(): String = "Misha"
}