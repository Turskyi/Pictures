package com.turskyi.gallery.models

import com.google.gson.annotations.SerializedName

data class OnlinePictureRepo(
    @SerializedName("color") val color: String? = "#000000",
    @SerializedName("urls") val urls: OnlinePictureUrl?,
    @SerializedName("user") val user: OnlinePictureUser
)