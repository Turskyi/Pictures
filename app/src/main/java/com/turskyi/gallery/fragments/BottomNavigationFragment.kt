package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.turskyi.gallery.R
import com.turskyi.gallery.data.GalleryConstants
import com.turskyi.gallery.databinding.FragmentBottomNavigationBinding

/**
 *  This fragment controls the "bottomNavigationView" which set another fragment for the whole screen
 *  */
class BottomNavigationFragment : Fragment(){

    private var _binding: FragmentBottomNavigationBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var fragmentId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomNavigationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            initFragment(item.itemId)
        }
        savedInstanceState?.let {
            fragmentId = it.getInt(GalleryConstants.KEY_WORD_FRAGMENT_ID)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(GalleryConstants.KEY_WORD_FRAGMENT_ID, fragmentId)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        initFragment(fragmentId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**  replaces container with a fragment */
    private fun initFragment(id: Int): Boolean {
        setVisibility(VISIBLE)
        val fragmentManager: FragmentTransaction = parentFragmentManager.beginTransaction()
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

   fun setVisibility(visibility: Int) {
        binding.bottomNavigationView.visibility = visibility
    }
}