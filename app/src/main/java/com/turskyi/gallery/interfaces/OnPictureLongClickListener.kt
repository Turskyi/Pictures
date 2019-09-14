package com.turskyi.gallery.interfaces

import com.turskyi.gallery.models.Picture
import com.turskyi.gallery.models.ViewType

interface OnPictureLongClickListener {
    fun addOnLongClick(picture: Picture)
    fun removeOnLongClick(picture: Picture, viewType: ViewType)
}