package com.turskyi.gallery.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.turskyi.gallery.R
import com.turskyi.gallery.data.GalleryConstants
import com.turskyi.gallery.fragments.OnlineDetailedFragment
import com.turskyi.gallery.models.OnlinePictureRepo
import kotlinx.android.synthetic.main.picture_list_item.view.*

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

    fun bindView(pictureRepo: OnlinePictureRepo) {
        //background of the name picture
//        itemView.setBackgroundColor(Color.parseColor(pictureRepo.color))
        // picture name
        itemView.pictureListName.text = pictureRepo.user.name
        Glide.with(itemView.context).load(pictureRepo.urls?.small)
            .into(itemView.pictureListPreviewIV)

        itemView.pictureListPreviewIV.setOnClickListener {
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