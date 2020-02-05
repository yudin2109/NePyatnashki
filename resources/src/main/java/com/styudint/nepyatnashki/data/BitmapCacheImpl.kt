package com.styudint.nepyatnashki.data

import android.graphics.Bitmap
import android.util.SparseArray
import java.lang.IllegalStateException
import javax.inject.Inject

class BitmapCacheImpl @Inject constructor() : BitmapCache {
    private var bitmap : Bitmap? = null

    private var x1 = 0
    private var y1 = 0

    private var x2 = 0
    private var y2 = 0

    private var size = 0

    private val cache = SparseArray<Bitmap>()

    override fun initialize(bitmap: Bitmap) {
        this.bitmap = bitmap
    }

    override fun setupSizeBounds(x1: Int, y1: Int, x2: Int, y2: Int) {
        this.x1 = x1
        this.y1 = y1
        this.x2 = x2
        this.y2 = y2
        invalidateCache()
        recalcSize()
    }

    override fun getBitmapForId(id: Int): Bitmap {
        if (cache.get(id) == null) {
            if (bitmap == null)
                throw IllegalStateException("Bitmap cannot be null")
            if (bitmap != null) {
                val current = Bitmap.createBitmap(bitmap!!,
                    (id % 4) * size / 4,
                    (id / 4) * size / 4,
                    size / 4,
                    size / 4)
                cache.put(id, current)
            }
        }
        return cache[id]
    }

    private fun recalcSize() {
        size = x2 - x1
        if (size != y2 - y1)
            throw IllegalStateException("Image should be square")
    }

    private fun invalidateCache() {
        cache.clear()
    }
}