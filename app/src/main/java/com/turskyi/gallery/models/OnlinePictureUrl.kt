package com.turskyi.gallery.models

import com.google.gson.annotations.SerializedName

data class OnlinePictureUrl(
    @SerializedName("raw") val raw: String?,
    @SerializedName("full") val full: String?,
    @SerializedName("small") val small: String?
)