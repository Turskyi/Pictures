package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.turskyi.gallery.R
import com.turskyi.gallery.data.GalleryConstants
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*

/** This fragment controls the "bottomNavigationView" which set another fragment for the whole screen */
class BottomNavigationFragment : Fragment(R.layout.fragment_bottom_navigation){

    private var fragmentId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            initFragment(item.itemId)
        }
    }

    override fun onResume() {
        super.onResume()
        initFragment(fragmentId)
    }

    /**  replaces container with a fragment */
    private fun initFragment(id: Int): Boolean {
        val fragmentManager: FragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentId = id

        val fragment: Fragment = when (id) {
            R.id.picturesMenu -> PicturesFragment()
            R.id.foldersMenu -> FoldersFragment()
            R.id.onlineMenu -> OnlinePicturesFragment()
            else -> PicturesFragment()
        }

        fragmentManager.replace(R.id.container, fragment).commit()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(GalleryConstants.KEY_WORD_FRAGMENT_ID, fragmentId)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        savedInstanceState?.let {
            fragmentId = it.getInt(GalleryConstants.KEY_WORD_FRAGMENT_ID)
        }
    }
}