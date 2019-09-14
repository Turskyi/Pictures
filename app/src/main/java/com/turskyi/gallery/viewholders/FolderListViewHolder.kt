package com.turskyi.gallery.viewholders

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.turskyi.gallery.R
import com.turskyi.gallery.data.GalleryConstants
import com.turskyi.gallery.fragments.PicturesInFolderFragment
import com.turskyi.gallery.models.Folder
import kotlinx.android.synthetic.main.folder_list_item.view.*
import java.io.File

class FolderListViewHolder(itemView: View, private val context: Context) :
    RecyclerView.ViewHolder(itemView) {

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(
            R.layout.folder_list_item,
            parent,
            false
        ),
        parent.context
    )

    val previewIV: ImageView =
        itemView.findViewById(com.turskyi.gallery.R.id.folderPreviewIV)
    val selectedFolder: ImageView =
        itemView.findViewById(com.turskyi.gallery.R.id.selectedFolder)

    fun bindView(folder: Folder) {
        /** Makes the cover of a folder with a picture */
        val file = File(folder.firstPicturePath)
        itemView.folderName.text = folder.name
        val uri: Uri = Uri.fromFile(file)
        Glide.with(context).load(uri).into(itemView.folderPreviewIV)

        itemView.folderPreviewIV.setOnClickListener {
            val fragmentManager: FragmentTransaction =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val picturesInFolderFragment =
                PicturesInFolderFragment(folder)
            fragmentManager
                .replace(R.id.container, picturesInFolderFragment, GalleryConstants.TAG_PICS_IN_FOLDER)
                .addToBackStack(GalleryConstants.TAG_PICS_IN_FOLDER).commit()
        }

        itemView.setOnClickListener {
            val fragmentManager: FragmentTransaction =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val picturesInFolderFragment =
                PicturesInFolderFragment(folder)
            fragmentManager
                .replace(R.id.container, picturesInFolderFragment, GalleryConstants.TAG_PICS_IN_FOLDER)
                .addToBackStack(GalleryConstants.TAG_PICS_IN_FOLDER).commit()
        }
    }
}