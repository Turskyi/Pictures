package com.turskyi.gallery.interfaces

import com.turskyi.gallery.models.PictureUri
import com.turskyi.gallery.models.ViewType

interface OnPictureLongClickListener {
    fun addOnLongClick(pictureUri: PictureUri)
    fun removeOnLongClick(pictureUri: PictureUri, viewType: ViewType)
}