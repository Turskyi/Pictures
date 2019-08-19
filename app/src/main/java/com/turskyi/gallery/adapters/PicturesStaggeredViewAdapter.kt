package com.turskyi.gallery.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.turskyi.gallery.DetailedActivity
import com.turskyi.gallery.R
import com.turskyi.gallery.data.Constants.KEY_WORD_PATH
import com.turskyi.gallery.models.GalleryPicture
import com.turskyi.gallery.models.ViewTypes
import java.io.File

//TODO в котліні не треба створювати функцію для заповнення масива, під його оголошенням можна написати
// set(value) {
//  field = value
//  notifyDataSetChanged() }

class PicturesStaggeredViewAdapter(private var picturesList: MutableList<GalleryPicture>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var viewType: ViewTypes = ViewTypes.STAGGERED

    /** set  the viewType */
    override fun getItemViewType(position: Int): Int {
        return when (viewType) {
            ViewTypes.STAGGERED -> ViewTypes.STAGGERED.id
            else -> ViewTypes.LINEAR.id
        }
    }

    /** switch between layouts */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {
        return when (viewType) {
            ViewTypes.LINEAR.id -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.picture_list_item, parent, false)
                PictureViewHolder(view, parent.context)
            } else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.picture_staggered_item, parent, false)
                PictureViewHolder(view, parent.context)
            }
        }
    }

    override fun getItemCount(): Int {
        picturesList ?: run {
            return 0
        }
        return picturesList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? PictureViewHolder)?.bindView(picturesList!![position])
    }

    class PictureViewHolder(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(itemView) {

        //TODO холдер є тільки для доступу до айтемів, в ньому не відбувається жодних оголошень, бо вони мінливі
        // занесення даних повинне відбуватися в он бінд, коли холдер повертається на екран
        // крім того об'єкти холдери можуть використовуватися повторно з іншими ресурсами. роблячи так ти блокуєш цю функцію

        private val pictureNameTV: TextView = itemView.findViewById(R.id.pictureName)
        private val previewIV: ImageView = itemView.findViewById(R.id.picturePreviewIV)
        private val selectedImage: ImageView = itemView.findViewById(R.id.selectedPicture)

        fun bindView(galleryPicture: GalleryPicture) {
            val file = File(galleryPicture.path)
            pictureNameTV.text = file.name
            val uri: Uri = Uri.fromFile(file)
            Glide.with(context).load(uri).into(previewIV)
            previewIV.setImageBitmap(BitmapFactory.decodeFile(galleryPicture.path))

            previewIV.setOnClickListener {

                // temporary method to send to DetailedActivity
                val intent = Intent(context, DetailedActivity::class.java)
                intent.putExtra(KEY_WORD_PATH, galleryPicture.path)
                context.startActivity(intent)

                //some kind of method to send data to second activity, but I do not know how to use it
//                startActivityForResult(context.applicationContext as Activity, intent, 1, /* what should be here?*/)

                //suppose to send to fragment, but I do not know how to implement it
//                val intent = Intent(context, DetailedFragment::class.java)

                //attempt to make DetailedFragment, but it does not want to implement supportFragmentManager
//                val detailFragment = DetailedFragment()
//                detailFragment.arguments = intent.extras
//                val fragmentManager = supportFragmentManager.beginTransaction()
//                fragmentManager.add(android.R.id.content, detailFragment).commit()
            }

            /** making "check sign visible and invisible onLongClick */
            previewIV.setOnLongClickListener {
                if (selectedImage.visibility == View.INVISIBLE) {
                    selectedImage.visibility = View.VISIBLE
                    val viewAdapter: PicturesStaggeredViewAdapter? = null
                    viewAdapter?.notifyDataSetChanged()
// Here I tried to made a remove button when picture is clicked
                    val btnRemove: ImageView? = itemView.findViewById(R.id.btnViewChanger)
                    btnRemove?.setImageResource(R.drawable.ic_remove32)
                } else {
                    selectedImage.visibility = View.INVISIBLE
                }
                true
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

//some kind of method to send data to second activity, but I do not know how to use it
//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//             //?
//            }
//        }
//    }
}