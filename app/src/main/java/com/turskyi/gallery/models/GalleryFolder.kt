package com.turskyi.gallery.models

import android.net.Uri

data class GalleryFolder (
    val path: String, val name: String) {
    var isSelected: Boolean = false
    var uri: Uri? = null
}
