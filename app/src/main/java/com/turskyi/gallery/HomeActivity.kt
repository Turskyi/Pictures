package com.turskyi.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.turskyi.gallery.fragments.BottomNavigationFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*

open class HomeActivity : AppCompatActivity(R.layout.activity_home) {

    //TODO: Should I move this constant to constants class?
    companion object {
        private const val PERMISSION_EXTERNAL_STORAGE = 10001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* permission must be here, in "onCreate" */
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
        grantResult: IntArray
    ) {
        when (requestCode) {
            PERMISSION_EXTERNAL_STORAGE -> {

                /** If request is cancelled, the result array is empty. */
                if ((grantResult.isNotEmpty() && grantResult[0] == PackageManager.PERMISSION_GRANTED)) {
                    getPermissionView.visibility = View.GONE
                    showFragment()
                } else {
                    /** shows the "get permission view" */
                    getPermissionView.visibility = View.VISIBLE
                    getPermissionView.setOnClickListener {
                        requestPermission()
                    }
                }
            }
        }
    }

    private fun showFragment() {
        val homeFragment = BottomNavigationFragment()
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

        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.really_exit))
                .setMessage(getString(R.string.are_you_sure_you_want_to_exit))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(
                    android.R.string.yes
                ) { _, _ -> super@HomeActivity.onBackPressed() }.create().show()

        } else {
            bottomNavigationView?.visibility = View.VISIBLE
            supportFragmentManager.popBackStack()
        }
    }
}
