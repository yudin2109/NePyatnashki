package com.styudint.nepyatnashki.data

import android.net.Uri

data class ResourceInfo(private val resId: Int?, private val uri: Uri?) {
    companion object {
        fun fromResource(resId: Int) = ResourceInfo(resId, null)
        fun fromUri(uri: Uri) = ResourceInfo(null, uri)
    }

    fun isResource() = resId != null
    fun isUri() = uri != null

    fun getResource() = resId
    fun getUri() = uri
}