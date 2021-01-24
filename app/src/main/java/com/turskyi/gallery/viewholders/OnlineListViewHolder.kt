package com.turskyi.gallery.viewholders

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
import com.turskyi.gallery.fragments.OnlineDetailedFragment
import com.turskyi.gallery.models.OnlinePictureRepo

/**
 * OnlinePictureRepo list view holder.
 */
class OnlineListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(
            R.layout.picture_list_item,
            parent,
            false
        )
    )

    private val tvPictureListItemName: TextView = itemView.findViewById(R.id.pictureListName)
    val ivPictureListItemPreview: ImageView = itemView.findViewById(R.id.pictureListPreviewIV)
    val ivSelectedPicture: ImageView = itemView.findViewById(R.id.selectedPicture)

    fun bindView(pictureRepo: OnlinePictureRepo) {
        // picture name
        tvPictureListItemName.text = pictureRepo.user.name
        Glide.with(itemView.context).load(pictureRepo.urls?.small)
            .into(ivPictureListItemPreview)

        ivPictureListItemPreview.setOnClickListener {
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