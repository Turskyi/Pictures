package com.turskyi.gallery.models

import com.google.gson.annotations.SerializedName

data class OnlinePictureUser(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("name") val name: String,
    @SerializedName("portfolio_url") val portfolio_url: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("total_likes") val total_likes: Int,
    @SerializedName("total_photos") val totalPhotos: Int,
    @SerializedName("total_collection") val total_collection: Int,
    @SerializedName("profile_image") val profileImage: OnlinePictureUrl,
    @SerializedName("links") val links: UnsplashLinks
)