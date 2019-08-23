package com.turskyi.gallery.models

data class GalleryFolder  (
    val folderPath : String, val firstPicturePath: String, val name: String, var images: MutableList<GalleryPicture>) {
    var isSelected: Boolean = false
}
