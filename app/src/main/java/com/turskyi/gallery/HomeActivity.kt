package com.turskyi.gallery

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.turskyi.gallery.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_EXTERNAL_STORAGE = 10001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        /** permission must be here, in "onCreate" */
        checkPermission()
    }

    /** check if we have permission */
    private fun checkPermission() {
        val permissionGranted =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionGranted != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
        } else {
            showFragment()
        }
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
                    emptyView.visibility = View.GONE
                    showFragment()
                } else {
                    /** shows the "get permission view" */
                    emptyView.visibility = View.VISIBLE
                    emptyView.setOnClickListener {
                        requestPermission()
                    }
                }
            }
        }
    }

    private fun showFragment() {
        val homeFragment = HomeFragment()
        homeFragment.arguments = intent.extras
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.add(R.id.frameLayout, homeFragment).commit()
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray(),
            PERMISSION_EXTERNAL_STORAGE
        )
    }

    override fun onBackPressed() {

        /** here we allow to use this method in fragments */
        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.container)
        (fragment as? IOnBackPressed)?.onBackPressed()?.let {
            super.onBackPressed()
        }

        if (toolbarTitle.text != title) FileLiveSingleton.getInstance().setBackPath()
        else {
            btnArrowBack.visibility = View.INVISIBLE
            AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(
                    android.R.string.yes
                ) { _, _ -> super@HomeActivity.onBackPressed() }.create().show()
        }
    }
}