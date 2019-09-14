package com.turskyi.gallery.data

import androidx.recyclerview.widget.DiffUtil
import com.turskyi.gallery.models.Folder
import com.turskyi.gallery.models.OnlinePictureRepo
import com.turskyi.gallery.models.Picture

object DiffUtilComparators {
    val ONLINE_PICTURES_DIFF_CALLBACK =
        object : DiffUtil.ItemCallback<OnlinePictureRepo>() {
            override fun areContentsTheSame(
                oldItem: OnlinePictureRepo,
                newItem: OnlinePictureRepo
            ): Boolean {
                return true
            }
            override fun areItemsTheSame(
                oldItem: OnlinePictureRepo,
                newItem: OnlinePictureRepo
            ): Boolean =
                oldItem.urls == newItem.urls
        }
    val FOLDERS_DIFF_CALLBACK: DiffUtil.ItemCallback<Folder> =
        object : DiffUtil.ItemCallback<Folder>() {
            override fun areItemsTheSame(
                oldItem: Folder,
                newItem: Folder
            ): Boolean {
                return oldItem.folderPath == newItem.folderPath
            }

            override fun areContentsTheSame(
                oldItem: Folder,
                newItem: Folder
            ): Boolean {
                return oldItem.firstPicturePath == newItem.firstPicturePath
            }
        }
    val PICTURES_DIFF_CALLBACK: DiffUtil.ItemCallback<Picture> =
        object : DiffUtil.ItemCallback<Picture>() {
            override fun areItemsTheSame(
                oldItem: Picture,
                newItem: Picture
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Picture,
                newItem: Picture
            ): Boolean {
                return oldItem.path == newItem.path
            }
        }
}