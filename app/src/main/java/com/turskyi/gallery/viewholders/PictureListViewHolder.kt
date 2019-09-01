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
import com.turskyi.gallery.fragments.DetailedFragment
import com.turskyi.gallery.models.GalleryPicture
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

    //TODO холдер є тільки для доступу до айтемів, в ньому не відбувається жодних оголошень,
    // бо вони мінливі
    // занесення даних повинне відбуватися в он бінд, коли холдер повертається на екран
    // крім того об'єкти холдери можуть використовуватися повторно з іншими ресурсами.
    // роблячи так ти блокуєш цю функцію
    private val pictureNameTV: TextView =
        itemView.findViewById(R.id.pictureName)
    val previewIV: ImageView =
        itemView.findViewById(R.id.picturePreviewIV)
    val selectedImage: ImageView =
        itemView.findViewById(R.id.selectedPicture)

    fun bindView(galleryPicture: GalleryPicture) {
        val file = File(galleryPicture.path)
        pictureNameTV.text = file.name
        val uri: Uri = Uri.fromFile(file)
        Glide.with(context).load(uri).into(previewIV)
        previewIV.setImageBitmap(BitmapFactory.decodeFile(galleryPicture.path))

        previewIV.setOnClickListener {
            val fragmentManager: FragmentTransaction =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val detailedFragment = DetailedFragment(galleryPicture)
            fragmentManager
                .replace(R.id.container, detailedFragment, Constants.TAG_DETAILED_FRAGMENT)
                .addToBackStack(Constants.TAG_DETAILED_FRAGMENT).commit()
        }

        itemView.setOnClickListener {
            val fragmentManager: FragmentTransaction =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val detailedFragment = DetailedFragment(galleryPicture)
            fragmentManager
                .replace(R.id.container, detailedFragment, Constants.TAG_DETAILED_FRAGMENT)
                .addToBackStack(Constants.TAG_DETAILED_FRAGMENT).commit()
        }
    }
}