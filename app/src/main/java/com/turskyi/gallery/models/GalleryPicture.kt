package com.turskyi.gallery.models

import android.net.Uri

data class GalleryPicture(val path: String) {
    var isSelected = false
    var uri: Uri? = null
}