package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.turskyi.gallery.R
import com.turskyi.gallery.databinding.FragmentDetailedBinding
import com.turskyi.gallery.interfaces.IOnBackPressed
import com.turskyi.gallery.models.OnlinePictureRepo

class OnlineDetailedFragment(private val pictureRepo: OnlinePictureRepo) : Fragment(),
    IOnBackPressed {

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

        // loading the image thanks to its url
        Glide.with(this).load(pictureRepo.urls?.full).into(binding.ivEnlarged)
        binding.includedToolbar.btnViewChanger.visibility = GONE
    }

    override fun onBackPressed() {
        val fragmentActivity: FragmentActivity? = activity
        (fragmentActivity?.supportFragmentManager
            ?.findFragmentById(R.id.bottomNavigationView) as BottomNavigationFragment?)
            ?.setVisibility(View.VISIBLE)
        activity?.supportFragmentManager?.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
