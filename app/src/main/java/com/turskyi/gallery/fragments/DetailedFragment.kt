package com.turskyi.gallery.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.turskyi.gallery.R
import com.turskyi.gallery.databinding.FragmentDetailedBinding
import com.turskyi.gallery.models.PictureUri

class DetailedFragment(private val pictureUri: PictureUri) : Fragment() {

    private var _binding: FragmentDetailedBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentActivity: FragmentActivity? = activity
        (fragmentActivity?.supportFragmentManager
            ?.findFragmentById(R.id.bottomNavigationView) as BottomNavigationFragment?)
            ?.setVisibility(GONE)

        binding.includedToolbar.btnArrowBack.setOnClickListener {
            onBackPressed()
        }

        binding.includedToolbar.btnViewChanger.setImageResource(R.drawable.ic_remove32)

        /**
         * @Description Opens the pictureUri in full size
         *  */
        val uri: Uri = pictureUri.uri
        Glide.with(this).load(uri).into(binding.ivEnlarged)

        binding.includedToolbar.btnViewChanger.setOnClickListener {
//TODO: delete image
        }
    }

    private fun onBackPressed() {
        val fragmentActivity: FragmentActivity? = activity
        (fragmentActivity?.supportFragmentManager
            ?.findFragmentById(R.id.bottomNavigationView) as BottomNavigationFragment?)
            ?.setVisibility(VISIBLE)
        activity?.supportFragmentManager?.popBackStack()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}