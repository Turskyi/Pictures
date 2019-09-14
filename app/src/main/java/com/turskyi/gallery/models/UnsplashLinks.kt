package com.turskyi.gallery.models

import com.google.gson.annotations.SerializedName

data class UnsplashLinks(
    @SerializedName("self") val self: String,
    @SerializedName("html") val html: String,
    @SerializedName("photos") val photos: String?,
    @SerializedName("likes") val likes: String?,
    @SerializedName("portfolio") val portfolio: String?,
    @SerializedName("download") val download: String?,
    @SerializedName("download_location") val download_location: String?
)
