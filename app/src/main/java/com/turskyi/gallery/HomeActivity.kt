package com.turskyi.gallery

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

class HomeActivity : AppCompatActivity() {

    private var fragmentId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*  Set the content of the activity to use the activity_home.xml layout file */
        setContentView(R.layout.activity_home)

//        @Suppress("UNUSED_VARIABLE")
//        val binding = DataBindingUtil.setContentView<ViewDataBinding>(this, R.layout.activity_home)

        bottom_navigation_view.setOnNavigationItemSelectedListener { item ->
            initFragment(item.itemId)
        }
    }

    override fun onResume() {
        super.onResume()
        initFragment(fragmentId)
    }

    private fun initFragment(id: Int): Boolean {
        val fragmentManager = supportFragmentManager.beginTransaction()

        fragmentId = id

        val fragment: Fragment = when (id) {
            R.id.pictures_menu -> PicturesFragment()
            R.id.folders_menu -> FoldersFragment()
            else -> PicturesFragment()
        }

        fragmentManager.replace(R.id.activity_main, fragment).commit()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("fragmentId", fragmentId)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        fragmentId = savedInstanceState.getInt("fragmentId")
    }

    override fun onBackPressed() {
        btn_arrow_back.visibility = View.VISIBLE
        if (toolbar_title.text == title) {
            btn_arrow_back.visibility = View.INVISIBLE
            AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(
                    android.R.string.yes
                ) { _, _ -> super@HomeActivity.onBackPressed() }.create().show()
        } else FileLiveSingleton.getInstance().setBackPath()
    }
}