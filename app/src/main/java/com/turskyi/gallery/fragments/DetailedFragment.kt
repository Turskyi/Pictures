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
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.turskyi.gallery.R
import com.turskyi.gallery.R.drawable
import com.turskyi.gallery.R.layout
import com.turskyi.gallery.data.Constants
import com.turskyi.gallery.models.GalleryPicture
import kotlinx.android.synthetic.main.fragment_detailed.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

class DetailedFragment(private val galleryPicture: GalleryPicture) : Fragment() {

    private var fragmentId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout.fragment_detailed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnArrowBack.setOnClickListener {
            initFragment(fragmentId)
        }

        btnViewChanger.setImageResource(drawable.ic_remove32)

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

    private fun initFragment(id: Int): Boolean {
        val fragmentManager: FragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentId = id
        val fragment: Fragment = if (id == R.id.picturesMenu) PicturesFragment()
        else PicturesInFolderFragment(null)
        fragmentManager.replace(R.id.container, fragment).commit()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(Constants.KEY_WORD_FRAGMENT_ID, fragmentId)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        savedInstanceState?.let {
            fragmentId = savedInstanceState.getInt(Constants.KEY_WORD_FRAGMENT_ID)
        }
    }
}