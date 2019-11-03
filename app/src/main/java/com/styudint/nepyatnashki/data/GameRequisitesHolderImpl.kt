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
import com.styudint.nepyatnashki.R
import javax.inject.Inject


class GameRequisitesHolderImpl constructor(private val ctx: Context) : GameRequisitesHolder {
    private val liveData = MutableLiveData<Bitmap>()
    private var currentInfo: ResourceInfo? = null
    override var Height: Int = 4
    override var Width: Int = 4

    init {
        loadResource(R.drawable.test_misha_0)
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