package com.turskyi.gallery.viewholders

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.turskyi.gallery.R
import com.turskyi.gallery.data.GalleryConstants
import com.turskyi.gallery.fragments.PicturesInFolderFragment
import com.turskyi.gallery.models.Folder
import java.io.File

class FolderGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(
            R.layout.folder_item, parent,
            false
        )
    )

    private val tvFolderName: TextView = itemView.findViewById(R.id.folderName)
    val ivFolderPreview: ImageView = itemView.findViewById(R.id.folderPreviewIV)
    val ivFolderSelected: ImageView = itemView.findViewById(R.id.selectedFolder)

    fun bindView(folder: Folder) {
        /** Makes the cover of a folder with a picture */
        val file = File(folder.firstPicturePath)
        tvFolderName.text = folder.name
        val uri: Uri = Uri.fromFile(file)
        Glide.with(itemView.context).load(uri).into(ivFolderPreview)

        ivFolderPreview.setOnClickListener {
            val fragmentManager: FragmentTransaction =
                (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val picturesInFolderFragment =
                PicturesInFolderFragment(folder)
            fragmentManager
                .replace(
                    R.id.container,
                    picturesInFolderFragment,
                    GalleryConstants.TAG_PICS_IN_FOLDER
                )
                .addToBackStack(GalleryConstants.TAG_PICS_IN_FOLDER).commit()
        }

        itemView.setOnClickListener {
            val fragmentManager: FragmentTransaction =
                (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val picturesInFolderFragment =
                PicturesInFolderFragment(folder)
            fragmentManager
                .replace(
                    R.id.container,
                    picturesInFolderFragment,
                    GalleryConstants.TAG_PICS_IN_FOLDER
                )
                .addToBackStack(GalleryConstants.TAG_PICS_IN_FOLDER).commit()
        }
    }
}