package com.styudint.nepyatnashki.data

import android.graphics.Bitmap

interface BitmapCache {
    fun initialize(bitmap: Bitmap)

    fun isInitialized(): Boolean
    var width: Int
    var height: Int

    fun setupSizeBounds(x1: Int, y1: Int, x2: Int, y2: Int)
    fun setupSizes(width: Int, height: Int)

    fun getBitmapForId(id: Int): Bitmap
}