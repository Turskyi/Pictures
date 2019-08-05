package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.turskyi.gallery.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment()
//, IOnBackPressed
{
    private var fragmentId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationView.setOnNavigationItemSelectedListener {item ->
            initFragment(item.itemId)
        }
    }

    override fun onResume() {
        super.onResume()
        initFragment(fragmentId)
    }

    private fun initFragment(id: Int): Boolean {
        val fragmentManager: FragmentTransaction = fragmentManager!!.beginTransaction()

        fragmentId = id

        val fragment: Fragment = when (id) {
            R.id.pictures_menu -> PicturesFragment()
            R.id.folders_menu -> FoldersFragment()
            else -> PicturesFragment()
        }

        fragmentManager.replace(R.id.container, fragment).commit()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("fragmentId", fragmentId)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            fragmentId = savedInstanceState.getInt("fragmentId")
        }
    }

//    override fun onBackPressed() {
//        btnArrowBack.visibility = View.VISIBLE
////     return if (toolbarTitle.text.equals(R.string.app_name)) {
//        return if (toolbarTitle.text == toolbarTitle.toString()) {
//            btnArrowBack.visibility = View.INVISIBLE
//            AlertDialog.Builder(activity?.applicationContext!!)
//                .setTitle("Really Exit?")
//                .setMessage("Are you sure you want to exit?")
//                .setNegativeButton(android.R.string.no, null)
//                .setPositiveButton(
//                    android.R.string.yes
//                ) { _, _ -> onBackPressed() }.create().show()
//        } else
//            FileLiveSingleton.getInstance().setBackPath()
//    }
}