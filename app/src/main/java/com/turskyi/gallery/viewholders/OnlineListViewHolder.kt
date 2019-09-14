package com.turskyi.gallery.viewholders

import android.content.Context
import android.graphics.Color
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
import com.turskyi.gallery.models.OnlinePictureRepo
import com.turskyi.gallery.fragments.OnlineDetailedFragment
import kotlinx.android.synthetic.main.picture_list_item.view.*

/**
 * OnlinePictureRepo list view holder.
 */
class OnlineListViewHolder(itemView: View, private val context: Context) :
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

    fun bindView(pictureRepo: OnlinePictureRepo) {
        //background of the name picture
//        itemView.setBackgroundColor(Color.parseColor(pictureRepo.color))
        // picture name
        itemView.pictureName.text = pictureRepo.user.name
        Glide.with(context).load(pictureRepo.urls?.small).into(itemView.picturePreviewIV)

        itemView.picturePreviewIV.setOnClickListener {
            val fragmentManager: FragmentTransaction =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val detailedFragment = OnlineDetailedFragment(pictureRepo)
            fragmentManager
                .replace(R.id.container, detailedFragment, GalleryConstants.TAG_DETAILED_FRAGMENT)
                .addToBackStack(GalleryConstants.TAG_DETAILED_FRAGMENT).commit()
        }

        itemView.setOnClickListener {
            val fragmentManager: FragmentTransaction =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val detailedFragment = OnlineDetailedFragment(pictureRepo)
            fragmentManager
                .replace(R.id.container, detailedFragment, GalleryConstants.TAG_DETAILED_FRAGMENT)
                .addToBackStack(GalleryConstants.TAG_DETAILED_FRAGMENT).commit()
        }
    }
}