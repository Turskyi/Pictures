package com.turskyi.gallery.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.turskyi.gallery.FileLiveSingleton
import com.turskyi.gallery.IOnBackPressed
import com.turskyi.gallery.R
import com.turskyi.gallery.R.drawable
import com.turskyi.gallery.R.layout
import kotlinx.android.synthetic.main.toolbar.*

class DetailFragment : Fragment(), IOnBackPressed {
    private lateinit var aPictureIV: ImageView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnArrowBack.setOnClickListener {
            onBackPressed()
        }

        btnViewChanger.setImageResource(drawable.ic_remove32)

        aPictureIV = view.findViewById(R.id.imageViewEnlarged)

        val aBundle: Bundle? = activity?.intent!!.extras
        aBundle?.let {
            val aBitmap = BitmapFactory.decodeFile(aBundle.getString("File"))
            aPictureIV.setImageBitmap(aBitmap)
        }

        btnViewChanger.setOnClickListener {
            Toast.makeText(activity, "I want to delete this picture", Toast.LENGTH_LONG).show()

        }
    }
    override fun onBackPressed() {
        btnArrowBack.visibility = View.INVISIBLE
            FileLiveSingleton.getInstance().setBackPath()
    }
}