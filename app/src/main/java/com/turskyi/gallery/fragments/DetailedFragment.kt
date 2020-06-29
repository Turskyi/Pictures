package com.turskyi.gallery.fragments

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.turskyi.gallery.models.PictureUri
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_detailed.*
import kotlinx.android.synthetic.main.toolbar.*

class DetailedFragment(private val pictureUri: PictureUri) :
    Fragment(com.turskyi.gallery.R.layout.fragment_detailed) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentActivity: FragmentActivity? = activity
        fragmentActivity?.bottomNavigationView?.visibility = View.GONE

        btnArrowBack.setOnClickListener {
            onBackPressed()
        }

        btnViewChanger.setImageResource(com.turskyi.gallery.R.drawable.ic_remove32)

        /**
         * @Description Opens the pictureUri in full size
         *  */
        val uri: Uri = pictureUri.uri
        Glide.with(this).load(uri).into(ivEnlarged)

//another option to show the pictureUri without Glide
//        imageViewEnlarged.setImageURI(uri)

        btnViewChanger.setOnClickListener {
//            if (file.exists()) {
//                val id = pictureUri.id
//                val deleteUri: Uri = ContentUris
//                    .withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//                activity?.contentResolver?.delete(deleteUri, null, null)
//                onBackPressed()
//            } else {
//                Toast.makeText(context, getString(com.turskyi.gallery.R.string.picture_does_not_exist), Toast.LENGTH_LONG)
//                    .show()
//            }
        }
    }

    private fun onBackPressed() {
        activity?.bottomNavigationView?.visibility = View.VISIBLE
        activity?.supportFragmentManager?.popBackStack()
    }
}