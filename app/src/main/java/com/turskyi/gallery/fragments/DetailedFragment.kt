package com.turskyi.gallery.fragments

import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.turskyi.gallery.interfaces.IOnBackPressed
import com.turskyi.gallery.models.Picture
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_detailed.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

class DetailedFragment(private val picture: Picture) :
    Fragment(com.turskyi.gallery.R.layout.fragment_detailed), IOnBackPressed {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentActivity: FragmentActivity? = activity
        fragmentActivity?.bottomNavigationView?.visibility = View.GONE

        btnArrowBack.setOnClickListener {
            onBackPressed()
        }

        btnViewChanger.setImageResource(com.turskyi.gallery.R.drawable.ic_remove32)

        /** Opens the picture in full size*/
        val imagePath = picture.path
        val file = File(imagePath)
        val uri: Uri = Uri.fromFile(file)
        Glide.with(this).load(uri).into(imageViewEnlarged)

//another option to show the picture without Glide
//        imageViewEnlarged.setImageURI(uri)

        btnViewChanger.setOnClickListener {
            if (file.exists()) {
                val id = picture.id
                val deleteUri: Uri = ContentUris
                    .withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                activity?.contentResolver?.delete(deleteUri, null, null)
                onBackPressed()
            } else {
                Toast.makeText(context, getString(com.turskyi.gallery.R.string.picture_does_not_exist), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onBackPressed() {
        activity?.bottomNavigationView?.visibility = View.VISIBLE
        //TODO: make the ability to remember the changes which happened
        // before changes of the fragment
        fragmentManager?.popBackStack()
    }
}