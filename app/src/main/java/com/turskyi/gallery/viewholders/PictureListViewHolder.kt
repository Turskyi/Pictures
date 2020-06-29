package com.turskyi.gallery.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.R
import kotlinx.android.synthetic.main.picture_list_item.view.*

class PictureListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(
            R.layout.picture_list_item,
            parent,
            false
        )
    )
    
    val pictureListPreviewIV: ImageView = itemView.pictureListPreviewIV
    val selectedPicture: ImageView = itemView.selectedPicture
}