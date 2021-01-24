package com.turskyi.gallery.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.R

class PictureListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(
            R.layout.picture_list_item,
            parent,
            false
        )
    )
    
    val ivPictureListItemPreview: ImageView = itemView.findViewById(R.id.pictureListPreviewIV)
    val ivSelectedPicture: ImageView = itemView.findViewById(R.id.selectedPicture)
}