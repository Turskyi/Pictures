package com.turskyi.gallery.interfaces

import com.turskyi.gallery.models.GalleryFolder

interface OnFolderClickListener {
    fun addOnClick(galleryFolder: GalleryFolder)
    fun removeOnClick(galleryFolder: GalleryFolder)
}