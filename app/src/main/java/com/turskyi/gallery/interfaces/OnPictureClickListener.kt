package com.turskyi.gallery.interfaces

import com.turskyi.gallery.models.GalleryPicture

interface OnPictureClickListener {
    fun addOnClick(galleryPicture: GalleryPicture)
    fun removeOnClick(galleryPicture: GalleryPicture)
}