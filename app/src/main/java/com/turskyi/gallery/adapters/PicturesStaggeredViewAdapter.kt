package com.turskyi.gallery.adapters

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
import com.turskyi.gallery.fragments.DetailedFragment
import com.turskyi.gallery.interfaces.OnPictureClickListener
import com.turskyi.gallery.models.GalleryPicture
import com.turskyi.gallery.models.ViewTypes
import java.io.File

//TODO в котліні не треба створювати функцію для заповнення масива, під його оголошенням можна написати
// set(value) {
//  field = value
//  notifyDataSetChanged() }

class PicturesStaggeredViewAdapter(
    private var picturesList: MutableList<GalleryPicture>?,
    private val onItemClickListener: OnPictureClickListener?) :
    RecyclerView.Adapter<PicturesStaggeredViewAdapter.PicturesInFolderViewHolder>() {

    private var viewType: ViewTypes = ViewTypes.STAGGERED

    /** set  the viewType */
    override fun getItemViewType(position: Int): Int {
        return if (viewType == ViewTypes.STAGGERED) ViewTypes.STAGGERED.id
        else ViewTypes.LINEAR.id
    }

    /** switch between layouts */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesInFolderViewHolder {
        return if (viewType == ViewTypes.LINEAR.id) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.picture_list_item, parent, false)
            PicturesInFolderViewHolder(view, parent.context)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.picture_staggered_item, parent, false)
            PicturesInFolderViewHolder(view, parent.context)
        }
    }

    override fun getItemCount(): Int {
        picturesList ?: run {
            return 0
        }
        return picturesList!!.size
    }

    override fun onBindViewHolder(holder: PicturesInFolderViewHolder, position: Int) {
        /** making "check sign visible and invisible onLongClick */
        holder.previewIV.setOnLongClickListener {
            if (holder.selectedImage.visibility == View.INVISIBLE) {
                holder.selectedImage.visibility = View.VISIBLE
                onItemClickListener?.addOnClick(picturesList?.elementAt(position)!!)
            } else {
                holder.selectedImage.visibility = View.INVISIBLE
                onItemClickListener?.removeOnClick(picturesList?.elementAt(position)!!)
            }
            true
        }
        holder.bindView(picturesList!![position])
    }

    class PicturesInFolderViewHolder(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(itemView) {

        //TODO холдер є тільки для доступу до айтемів, в ньому не відбувається жодних оголошень, бо вони мінливі
        // занесення даних повинне відбуватися в он бінд, коли холдер повертається на екран
        // крім того об'єкти холдери можуть використовуватися повторно з іншими ресурсами. роблячи так ти блокуєш цю функцію
        private val pictureNameTV: TextView = itemView.findViewById(R.id.pictureName)
        val previewIV: ImageView = itemView.findViewById(R.id.picturePreviewIV)
        val selectedImage: ImageView = itemView.findViewById(R.id.selectedPicture)

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
                fragmentManager.replace(R.id.container, detailedFragment).commit()
            }

            /* the method bellow I am going to use later for unknown yet reason */
//            itemView.setOnLongClickListener {
//                if (selectedFolder.visibility == View.INVISIBLE) {
//                    selectedFolder.visibility = View.VISIBLE
//                } else {
//                    selectedFolder.visibility = View.INVISIBLE
//                }
//            }
        }
    }

    fun changeViewType() {
        viewType = when {
            viewType.id == ViewTypes.LINEAR.id -> ViewTypes.STAGGERED
            else -> ViewTypes.LINEAR
        }
        notifyDataSetChanged()
    }
}