package com.styudint.nepyatnashki.data

import android.graphics.Bitmap

interface BitmapCache {
    fun initialize(bitmap: Bitmap)

    fun isInitialized(): Boolean

    fun setupSizeBounds(x1: Int, y1: Int, x2: Int, y2: Int)

    fun getBitmapForId(id: Int): Bitmap
}