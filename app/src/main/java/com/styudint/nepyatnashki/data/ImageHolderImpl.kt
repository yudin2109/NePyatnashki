package com.styudint.nepyatnashki.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import com.styudint.nepyatnashki.R


class ImageHolderImpl constructor(private val ctx: Context) : ImageHolder {
    private val liveData = MutableLiveData<Bitmap>()
    private var currentInfo: ResourceInfo? = null

    init {
        loadResource(R.drawable.test_misha)
    }

    override fun loadImage(uri: Uri) {
        GlobalScope.launch {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = ctx.contentResolver.query(uri, filePathColumn, null, null, null) ?: return@launch
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()
            val bitmap = BitmapFactory.decodeFile(picturePath)
            currentInfo = ResourceInfo.fromUri(uri)
            liveData.postValue(bitmap)
        }
    }

    override fun loadResource(resId: Int) {
        GlobalScope.launch {
            var background = BitmapFactory.decodeResource(ctx.resources, resId)
            background = Bitmap.createScaledBitmap(background, background.width, background.height, false)
            currentInfo = ResourceInfo.fromResource(resId)
            liveData.postValue(background)
        }
    }

    override fun loadFromResourceInfo(info: ResourceInfo) {
        val uri = info.getUri()
        if (uri != null) {
            loadImage(uri)
            return
        }

        val resId = info.getResource()
        if (resId != null) {
            loadResource(resId)
        }
    }

    override fun info(): ResourceInfo? = currentInfo

    override fun bitmap(): LiveData<Bitmap> = liveData

}