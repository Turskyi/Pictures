package com.turskyi.gallery.interfaces

import com.turskyi.gallery.models.GalleryPicture
import com.turskyi.gallery.models.ViewType

interface OnPictureClickListener {
    fun addOnLongClick(galleryPicture: GalleryPicture)
    fun removeOnLongClick(galleryPicture: GalleryPicture, viewType: ViewType)
}