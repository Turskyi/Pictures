package com.turskyi.gallery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.R
import com.turskyi.gallery.models.GalleryPicture
import com.turskyi.gallery.models.ViewTypes

class GalleryPicturesAdapter(
    private var list: ArrayList<GalleryPicture?>,
    private var numberOfSelected: Int = 0,
    private var isGridEnum: ViewTypes = ViewTypes.LINEAR
) : RecyclerView.Adapter<GalleryViewHolder>() {
    private lateinit var onClick: (GalleryPicture) -> Unit

    /*   Useful method to provide delete feature. */
//    fun deletePicture(picture: GalleryPicture) {
//        deletePicture(list.indexOf(picture))
//    }
//
//    private fun deletePicture(position: Int) {
//        if (File(getItem(position)!!.path).delete()) {
//            list.removeAt(position)
//            notifyItemRemoved(position)
//        } else {
//            Log.e("GalleryPicturesAdapter", "Deletion Failed")
//        }
//    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GalleryViewHolder {
        val viewHolder = GalleryViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_item, p0, false))
        viewHolder.containerView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val picture = getItem(position)
                picture?.let { it1 -> onClick(it1) }
        }
        return viewHolder
    }

    private fun getItem(position: Int) = list[position]

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
//        (holder as? GalleryViewHolder)?.bindView(list[position]!!, aContext = aContext)
    }

    override fun getItemCount() = list.size

    fun getAllChecked(): ArrayList<GalleryPicture?> {
        val checkedFiles: ArrayList<GalleryPicture?> = ArrayList()
        for (aFile in list) {
            if (aFile?.isSelected!!) {
                numberOfSelected++
                checkedFiles.add(aFile)
                notifyDataSetChanged()
            }
        }
        notifyDataSetChanged()
        return checkedFiles
    }

    fun addNewFiles(list: List<GalleryPicture?>) {
        if (list.isNotEmpty()) {
            for (aFile in list) {
                this.list.add(aFile)
            }
            notifyDataSetChanged()
        }
    }

    fun changeViewType() {
        isGridEnum = if (isGridEnum.id == ViewTypes.LINEAR.id) {
            ViewTypes.GRID
        } else {
            ViewTypes.LINEAR
        }
        notifyDataSetChanged()
    }

    fun setNewList(list: ArrayList<GalleryPicture?>) {
        this.list = list
        notifyDataSetChanged()
    }
}