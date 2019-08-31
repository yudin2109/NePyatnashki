package com.styudint.nepyatnashki.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.styudint.nepyatnashki.NePyatnashkiApp
import com.styudint.nepyatnashki.R
import com.styudint.nepyatnashki.data.ImageHolder
import com.styudint.nepyatnashki.data.ResourceInfo
import kotlinx.android.synthetic.main.gallery_image.view.*
import kotlinx.android.synthetic.main.gallery_page_header.view.*
import kotlinx.android.synthetic.main.gallery_page_item.view.*
import javax.inject.Inject

class GalleryAdapter(private val activity: AppCompatActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    @Inject
    lateinit var imageHolder: ImageHolder

    init {
        (activity.applicationContext as NePyatnashkiApp).appComponent.inject(this)
    }

    companion object {
        const val HEADER = 0
        const val GALLERY_ITEM = 1
    }

    private val adapters = arrayListOf(
        Classic15GalleryAdapter(activity),
        LandscapeGalleryAdapter(activity),
        MishaGalleryAdapter(activity)
    )

    private var picked: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HEADER)
            return createHeader(parent)
        return createGalleryItem(parent)
    }

    override fun getItemCount(): Int {
        return 1 + adapters.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == HEADER) {
            holder as HeaderViewHolder
            holder.backButton.setOnClickListener {
                activity.finish()
            }
        }
        if (holder.itemViewType == GALLERY_ITEM) {
            holder as GalleryItem
            holder.bind(adapters[position - 1], LayoutInflater.from(activity))
        }
    }

    private fun picked(view: View, info: ResourceInfo) {
        picked?.let {
            it.findViewById<ImageView>(R.id.overlay).visibility = View.GONE
        }
        picked = view
        picked?.let {
            it.findViewById<ImageView>(R.id.overlay).visibility = View.VISIBLE
            imageHolder.loadFromResourceInfo(info)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return HEADER
        return GALLERY_ITEM
    }

    private fun createGalleryItem(parent: ViewGroup): BasicGalleryItem {
        val view = LayoutInflater.from(activity).inflate(R.layout.gallery_page_item, parent, false)
        return BasicGalleryItem(view)
    }

    private fun createHeader(parent: ViewGroup): HeaderViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.gallery_page_header, parent, false)
        return HeaderViewHolder(view)
    }

    interface GalleryItem {
        fun bind(adapter: GalleryImageAdapter, inflater: LayoutInflater)
    }

    inner class BasicGalleryItem(view: View) : RecyclerView.ViewHolder(view), GalleryItem {
        private val header: TextView = view.blockHeader
        private val miniGallery: LinearLayout = view.miniGalleryAnchor

        override fun bind(adapter: GalleryImageAdapter, inflater: LayoutInflater) {
            header.text = adapter.getName()
            miniGallery.removeAllViews()

            val rows = ArrayList<LinearLayout>()
            val amountOfRows = (adapter.amountOfImages() + 3) / 4

            for (i in 0 until(amountOfRows)) {
                val linearLayout = inflater.inflate(
                    R.layout.mini_gallery_row,
                    miniGallery,
                    false) as LinearLayout
                rows.add(linearLayout)
            }

            for (i in 0 until(adapter.amountOfImages())) {
                val view = inflater.inflate(R.layout.gallery_image, rows[i / 4], false)
                adapter.getBitmap(i).observe(activity, Observer {
                    view.image.setImageBitmap(it)
                })
                view.setOnClickListener {
                    picked(view, adapter.getResourceInfo(i))
                }
                if (adapter.getResourceInfo(i) == imageHolder.info()) {
                    view.findViewById<ImageView>(R.id.overlay).visibility = View.VISIBLE
                    picked = view
                }
                rows[i / 4].addView(view)
            }

            rows.forEach {
                miniGallery.addView(it)
            }
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val backButton: ImageButton = view.backButton
    }
}