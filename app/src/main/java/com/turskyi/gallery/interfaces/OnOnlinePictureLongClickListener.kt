package com.turskyi.gallery.interfaces

import com.turskyi.gallery.models.OnlinePictureRepo
import com.turskyi.gallery.models.ViewType

interface OnOnlinePictureLongClickListener {
    fun addOnLongClick(pictureRepo: OnlinePictureRepo)
    fun removeOnLongClick(pictureRepo: OnlinePictureRepo, viewType: ViewType)
}