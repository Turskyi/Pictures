package com.turskyi.gallery.data

import androidx.recyclerview.widget.DiffUtil
import com.turskyi.gallery.models.Folder
import com.turskyi.gallery.models.OnlinePictureRepo
import com.turskyi.gallery.models.PictureUri

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
    val PICTURES_DIFF_CALLBACK: DiffUtil.ItemCallback<PictureUri> =
        object : DiffUtil.ItemCallback<PictureUri>() {
            override fun areItemsTheSame(
                oldItem: PictureUri,
                newItem: PictureUri
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PictureUri,
                newItem: PictureUri
            ): Boolean {
                return oldItem.uri == newItem.uri
            }
        }
}