package com.turskyi.gallery.viewholders

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
import com.turskyi.gallery.fragments.OnlineDetailedFragment
import com.turskyi.gallery.models.OnlinePictureRepo

/**
 * @Description OnlinePictureRepo grid view holder.
 */
class OnlineGridViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(
            R.layout.picture_item,
            parent,
            false
        )
    )

    val ivPicturePreview: ImageView = itemView.findViewById(R.id.picturePreviewIV)
    val ivSelectedPicture: ImageView = itemView.findViewById(R.id.selectedPicture)

    fun bindView(pictureRepo: OnlinePictureRepo) {
        Glide.with(itemView.context).load(pictureRepo.urls?.small).into(ivPicturePreview)

        ivPicturePreview.setOnClickListener {
            val fragmentManager: FragmentTransaction =
                (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val detailedFragment = OnlineDetailedFragment(pictureRepo)
            fragmentManager
                .replace(R.id.container, detailedFragment, GalleryConstants.TAG_DETAILED_FRAGMENT)
                .addToBackStack(GalleryConstants.TAG_DETAILED_FRAGMENT).commit()
        }

        itemView.setOnClickListener {
            val fragmentManager: FragmentTransaction =
                (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val detailedFragment = OnlineDetailedFragment(pictureRepo)
            fragmentManager
                .replace(R.id.container, detailedFragment, GalleryConstants.TAG_DETAILED_FRAGMENT)
                .addToBackStack(GalleryConstants.TAG_DETAILED_FRAGMENT).commit()
        }
    }
}