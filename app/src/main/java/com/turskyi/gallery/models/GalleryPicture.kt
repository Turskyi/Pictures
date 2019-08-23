package com.turskyi.gallery.models

data class GalleryPicture(val path: String, val id: Long) {
    var isSelected = false
}