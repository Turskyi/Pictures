package com.turskyi.gallery.interfaces

import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.ViewType

interface OnFolderClickListener {
    fun addOnLongClick(galleryFolder: GalleryFolder)
    fun removeOnLongClick(galleryFolder: GalleryFolder, viewType: ViewType)
}