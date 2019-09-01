package com.turskyi.gallery.viewholders

import android.content.Context
import android.graphics.BitmapFactory
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
import com.turskyi.gallery.data.Constants
import com.turskyi.gallery.fragments.PicturesInFolderFragment
import com.turskyi.gallery.models.GalleryFolder
import java.io.File

class FolderGridViewHolder(itemView: View, private val context: Context) :
    RecyclerView.ViewHolder(itemView) {

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(
            R.layout.folder_item,
            parent,
            false
        ),
        parent.context
    )

    //TODO холдер є тільки для доступу до айтемів, в ньому не відбувається жодних оголошень,
    // бо вони мінливі
    // занесення даних повинне відбуватися в он бінд, коли холдер повертається на екран
    // крім того об'єкти холдери можуть використовуватися повторно з іншими ресурсами.
    // роблячи так ти блокуєш цю функцію
    private val folderNameTV: TextView =
        itemView.findViewById(com.turskyi.gallery.R.id.folderName)
    val previewIV: ImageView =
        itemView.findViewById(com.turskyi.gallery.R.id.folderPreviewIV)
    val selectedFolder: ImageView =
        itemView.findViewById(com.turskyi.gallery.R.id.selectedFolder)

    fun bindView(galleryFolder: GalleryFolder) {
        /** Makes the cover of a folder with a picture */
        val file = File(galleryFolder.firstPicturePath)
        folderNameTV.text = galleryFolder.name
        val uri: Uri = Uri.fromFile(file)
        Glide.with(context).load(uri).into(previewIV)
        previewIV.setImageBitmap(BitmapFactory.decodeFile(galleryFolder.firstPicturePath))

        previewIV.setOnClickListener {
            val fragmentManager: FragmentTransaction =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val picturesInFolderFragment = PicturesInFolderFragment(galleryFolder)
            fragmentManager
                .replace(R.id.container, picturesInFolderFragment, Constants.TAG_PICS_IN_FOLDER)
                .addToBackStack(Constants.TAG_PICS_IN_FOLDER).commit()
        }

        itemView.setOnClickListener {
            val fragmentManager: FragmentTransaction =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val picturesInFolderFragment = PicturesInFolderFragment(galleryFolder)
            fragmentManager
                .replace(R.id.container, picturesInFolderFragment, Constants.TAG_PICS_IN_FOLDER)
                .addToBackStack(Constants.TAG_PICS_IN_FOLDER).commit()
        }
    }
}