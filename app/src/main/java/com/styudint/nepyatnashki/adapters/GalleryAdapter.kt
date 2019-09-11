package com.styudint.nepyatnashki.adapters

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
import kotlinx.android.synthetic.main.basic_gallery_page_item.view.*
import kotlinx.android.synthetic.main.horizontal_scrollable_gallery_adapter.view.*
import javax.inject.Inject

class GalleryAdapter(private val activity: AppCompatActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    @Inject
    lateinit var imageHolder: ImageHolder

    init {
        (activity.applicationContext as NePyatnashkiApp).appComponent.inject(this)
    }

    companion object {
        const val EMPTY_ITEM = -1
        const val HEADER = 0
        const val BASIC_GALLERY_ITEM = 1
        const val SCROLLABLE_GALLERY_ITEM = 2

        const val DEFAULT_IMAGE_IN_ROW_COUNT = 4
    }

    private val adapters = arrayListOf(
        Classic15GalleryAdapter(activity),
            MishaGalleryAdapter(activity),
        LandscapeGalleryAdapter(activity)
    )

    private var picked: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HEADER)
            return createHeader(parent)
        if (viewType == BASIC_GALLERY_ITEM)
            return createBasicGalleryItem(parent)
        if (viewType == EMPTY_ITEM)
            return createEmptyItem(parent)
        return createHorizontalScrollableGalleryItem(parent)
    }

    override fun getItemCount(): Int {
        return 2 + adapters.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == HEADER) {
            holder as HeaderViewHolder
            holder.backButton.setOnClickListener {
                activity.finish()
            }
        }
        if (holder.itemViewType == BASIC_GALLERY_ITEM || holder.itemViewType == SCROLLABLE_GALLERY_ITEM) {
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
        if (position == 3)
            return SCROLLABLE_GALLERY_ITEM
        if (position == adapters.size + 1)
            return EMPTY_ITEM
        return BASIC_GALLERY_ITEM
    }

    private fun createEmptyItem(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.empty_gallery_item, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    private fun createBasicGalleryItem(parent: ViewGroup): BasicGalleryItem {
        val view = LayoutInflater.from(activity).inflate(R.layout.basic_gallery_page_item, parent, false)
        return BasicGalleryItem(view)
    }

    private fun createHorizontalScrollableGalleryItem(parent: ViewGroup) : HorizontalScrollableGalleryItem {
        val view = LayoutInflater.from(activity).inflate(R.layout.horizontal_scrollable_gallery_adapter, parent, false)
        return HorizontalScrollableGalleryItem(view)
    }

    private fun createHeader(parent: ViewGroup): HeaderViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.gallery_page_header, parent, false)
        return HeaderViewHolder(view)
    }

    private fun registerImage(anchorView: View, info: ResourceInfo) {
        if (info == imageHolder.info()) {
            anchorView.findViewById<ImageView>(R.id.overlay).visibility = View.VISIBLE
            picked = anchorView
        }

        anchorView.setOnClickListener {
            picked(anchorView, info)
        }
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
            val amountOfRows =
                (adapter.amountOfImages() + DEFAULT_IMAGE_IN_ROW_COUNT - 1) / DEFAULT_IMAGE_IN_ROW_COUNT

            for (i in 0 until(amountOfRows)) {
                val linearLayout = inflater.inflate(
                    R.layout.mini_gallery_row,
                    miniGallery,
                    false) as LinearLayout
                rows.add(linearLayout)
            }

            for (i in 0 until(adapter.amountOfImages())) {
                val view = inflater.inflate(
                    R.layout.gallery_image,
                    rows[i / DEFAULT_IMAGE_IN_ROW_COUNT],
                    false)
                adapter.getBitmap(i).observe(activity, Observer {
                    view.image.setImageBitmap(it)
                })
                registerImage(view, adapter.getResourceInfo(i))
                rows[i / DEFAULT_IMAGE_IN_ROW_COUNT].addView(view)
            }

            rows.forEach {
                miniGallery.addView(it)
            }
        }
    }

    inner class HorizontalScrollableGalleryItem(view: View) : RecyclerView.ViewHolder(view), GalleryItem {
        private val header: TextView = view.header
        private val imagesAnchor: LinearLayout = view.imagesAnchor

        override fun bind(adapter: GalleryImageAdapter, inflater: LayoutInflater) {
            header.text = adapter.getName()
            imagesAnchor.removeAllViews()
            for (i in 0 until adapter.amountOfImages()) {
                val imageFrame = inflater.inflate(R.layout.gallery_image, imagesAnchor, false)
                adapter.getBitmap(i).observe(activity, Observer {
                    if (it == null)
                        return@Observer
                    prepareViewLayout(imageFrame)
                    imageFrame.image.setImageBitmap(it)
                })
                registerImage(imageFrame, adapter.getResourceInfo(i))
                imagesAnchor.addView(imageFrame)
            }
        }

        private fun prepareViewLayout(view: View) {
            val params = LinearLayout.LayoutParams(
                header.width / (DEFAULT_IMAGE_IN_ROW_COUNT),
                header.width / (DEFAULT_IMAGE_IN_ROW_COUNT),
                1.0f)
            view.layoutParams = params
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val backButton: ImageButton = view.backButton
    }
}