package com.turskyi.gallery.fragments

import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.turskyi.gallery.interfaces.IOnBackPressed
import com.turskyi.gallery.models.GalleryPicture
import kotlinx.android.synthetic.main.fragment_detailed.*
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

class DetailedFragment(private val galleryPicture: GalleryPicture) : Fragment(), IOnBackPressed {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.turskyi.gallery.R.layout.fragment_detailed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentActivity: FragmentActivity? = activity
        fragmentActivity?.bottomNavigationView?.visibility = View.GONE

        btnArrowBack.setOnClickListener {
            onBackPressed()
        }

        btnViewChanger.setImageResource(com.turskyi.gallery.R.drawable.ic_remove32)

        /* First way to implement the picture on full screen with Glide*/
        val imagePath = galleryPicture.path
        val file = File(imagePath)
        val uri: Uri = Uri.fromFile(file)
        Glide.with(this).load(uri).into(imageViewEnlarged)

        /* Second way to open picture full screen without Glide */
//        val aBundle: Bundle? = activity?.intent!!.extras
//        aBundle?.let {
//            val aBitmap = BitmapFactory.decodeFile(aBundle.getString(imagePath))
//            imageViewEnlarged.setImageBitmap(aBitmap)
//        }

        val id = galleryPicture.id

        btnViewChanger.setOnClickListener {
            if (file.exists()) {
                val deleteUri: Uri = ContentUris
                    .withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                activity?.contentResolver?.delete(deleteUri, null, null)
            } else {
                Toast.makeText(context, "File is not exist", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        activity?.bottomNavigationView?.visibility = View.VISIBLE
        //TODO: what is going on when I press on "back press arrow"?
        // Why does not observer  receive the changes?
        // How can I fix this bug?
        fragmentManager?.popBackStack()
    }
}