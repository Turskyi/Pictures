package com.turskyi.gallery

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.turskyi.gallery.fragments.FoldersFragment
import com.turskyi.gallery.fragments.PicturesFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_EXTERNAL_STORAGE = 10001
    }

    private var fragmentId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*  Set the content of the activity to use the activity_home.xml layout file */
        setContentView(R.layout.activity_home)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
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

        fragmentManager.replace(R.id.container, fragment).commit()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_EXTERNAL_STORAGE -> {

                /** If request is cancelled, the result arrays are empty. */
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                    getPermission()
                }
                return
            }
        }
    }

    private fun getPermission() {
        ActivityCompat.requestPermissions(
            this,
            listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray(),
            PERMISSION_EXTERNAL_STORAGE
        )
    }

    override fun onBackPressed() {

        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.container)
        (fragment as? IOnBackPressed)?.onBackPressed()?.let {
            super.onBackPressed()
        }

        btnArrowBack.visibility = View.VISIBLE
        if (toolbarTitle.text == title) {
            btnArrowBack.visibility = View.INVISIBLE
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