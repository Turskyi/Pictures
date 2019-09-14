package com.turskyi.gallery.interfaces

import com.turskyi.gallery.models.Folder
import com.turskyi.gallery.models.ViewType

interface OnFolderLongClickListener {
    fun addOnLongClick(folder: Folder)
    fun removeOnLongClick(folder: Folder, viewType: ViewType)
}