package com.turskyi.gallery.model

data class MyFile (
    val path: String,
    val name: String,
    val extension: String?,
    val imageFile: MyFile?,
    var isChecked: Boolean
)