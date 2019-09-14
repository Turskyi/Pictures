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
import com.turskyi.gallery.fragments.DetailedFragment
import com.turskyi.gallery.models.Picture
import kotlinx.android.synthetic.main.picture_list_item.view.*
import java.io.File

class PictureListViewHolder(itemView: View, private val context: Context) :
    RecyclerView.ViewHolder(itemView) {

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(
            R.layout.picture_list_item,
            parent,
            false
        ),
        parent.context
    )

    val previewIV: ImageView =
        itemView.findViewById(R.id.picturePreviewIV)
    val selectedImage: ImageView =
        itemView.findViewById(R.id.selectedPicture)

    fun bindView(picture: Picture) {
        val file = File(picture.path)
        itemView.pictureName.text = file.name
        val uri: Uri = Uri.fromFile(file)
        Glide.with(context).load(uri).into(itemView.picturePreviewIV)

        itemView.picturePreviewIV.setOnClickListener {
            val fragmentManager: FragmentTransaction =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val detailedFragment = DetailedFragment(picture)
            fragmentManager
                .replace(R.id.container, detailedFragment, GalleryConstants.TAG_DETAILED_FRAGMENT)
                .addToBackStack(GalleryConstants.TAG_DETAILED_FRAGMENT).commit()
        }

        itemView.setOnClickListener {
            val fragmentManager: FragmentTransaction =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val detailedFragment = DetailedFragment(picture)
            fragmentManager
                .replace(R.id.container, detailedFragment, GalleryConstants.TAG_DETAILED_FRAGMENT)
                .addToBackStack(GalleryConstants.TAG_DETAILED_FRAGMENT).commit()
        }
    }
}