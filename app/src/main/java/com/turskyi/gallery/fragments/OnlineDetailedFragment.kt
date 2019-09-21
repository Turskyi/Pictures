package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.turskyi.gallery.R
import com.turskyi.gallery.models.OnlinePictureRepo
import com.turskyi.gallery.interfaces.IOnBackPressed
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_detailed.*
import kotlinx.android.synthetic.main.toolbar.*

class OnlineDetailedFragment(private val pictureRepo: OnlinePictureRepo) : Fragment(R.layout.fragment_detailed),
    IOnBackPressed {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentActivity: FragmentActivity? = activity
        fragmentActivity?.bottomNavigationView?.visibility = View.GONE

        btnArrowBack.setOnClickListener {
            onBackPressed()
        }

        // loading the image thanks to its url
        Glide.with(this).load(pictureRepo.urls?.full).into(imageViewEnlarged)
        btnViewChanger.visibility = GONE
    }

        override fun onBackPressed() {
            activity?.bottomNavigationView?.visibility = View.VISIBLE
            //TODO: to make observer  receive the changes
            // that it was a different layout before it was open?
            // Fix this bug.
            fragmentManager?.popBackStack()
        }
}
