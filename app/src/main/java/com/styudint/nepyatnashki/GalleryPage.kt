package com.styudint.nepyatnashki

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.styudint.nepyatnashki.adapters.GalleryAdapter
import kotlinx.android.synthetic.main.gallery_page.*

class GalleryPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery_page)

        recyclerView.adapter = GalleryAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fab.setOnClickListener {
            play()
        }
    }

    private fun play() {
        startActivity(Intent(this, GameActivity::class.java))
    }
}