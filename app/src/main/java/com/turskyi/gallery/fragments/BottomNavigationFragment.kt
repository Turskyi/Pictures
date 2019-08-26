package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.turskyi.gallery.R
import com.turskyi.gallery.data.Constants
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*

/** This fragment controls the "bottomNavigationView" which set another fragment for the whole screen */
class BottomNavigationFragment : Fragment(){

    private var fragmentId = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_navigation, container, false)
    }

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
            else -> PicturesFragment()
        }

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
            fragmentId = it.getInt(Constants.KEY_WORD_FRAGMENT_ID)
        }
    }
}