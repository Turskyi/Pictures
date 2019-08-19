package com.turskyi.gallery.fragments

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.turskyi.gallery.data.Constants
import com.turskyi.gallery.FileLiveSingleton
import com.turskyi.gallery.IOnBackPressed
import com.turskyi.gallery.R.drawable
import com.turskyi.gallery.R.layout
import kotlinx.android.synthetic.main.fragment_detailed.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

//I do not use this fragment because I do not know how to,
// so for now I use DetailedActivity instead
class DetailedFragment : Fragment(), IOnBackPressed {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layout.fragment_detailed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnArrowBack.setOnClickListener {
            onBackPressed()
        }

        btnViewChanger.setImageResource(drawable.ic_remove32)

        /* First way to implement the picture on full screen with Glide*/
        val path = activity?.intent!!.getStringExtra(Constants.KEY_WORD_PATH)
        val file = File(path!!)
        val uri: Uri = Uri.fromFile(file)
        Glide.with(this).load(uri).into(imageViewEnlarged)

        /* Second way to open picture full screen without Glide */
        val aBundle: Bundle? = activity?.intent!!.extras
        aBundle?.let {
            val aBitmap = BitmapFactory.decodeFile(aBundle.getString(Constants.KEY_WORD_PATH))
            imageViewEnlarged.setImageBitmap(aBitmap)
        }

        btnViewChanger.setOnClickListener {
            Toast.makeText(activity, "I want to delete this picture", Toast.LENGTH_LONG).show()
        }
    }

    /* I need the following method otherwise I will not be able to inherit the interface "OnBackPressed" */
    override fun onBackPressed() {
        btnArrowBack.visibility = View.INVISIBLE
        FileLiveSingleton.getInstance().setBackPath()
    }
}