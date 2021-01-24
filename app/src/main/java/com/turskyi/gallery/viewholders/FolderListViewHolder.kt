package com.turskyi.gallery.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.R

class FolderListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(
            R.layout.folder_list_item,
            parent,
            false
        )
    )

    val tvListItemName: TextView = itemView.findViewById(R.id.folderListName)
    val ivFolderListItemPreview: ImageView = itemView.findViewById(R.id.folderListPreviewIV)
    val ivSelectedFolder: ImageView = itemView.findViewById(R.id.selectedFolder)
}