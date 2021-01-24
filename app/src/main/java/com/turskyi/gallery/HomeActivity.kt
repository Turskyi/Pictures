package com.turskyi.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.turskyi.gallery.data.GalleryConstants.PERMISSION_EXTERNAL_STORAGE
import com.turskyi.gallery.databinding.ActivityHomeBinding
import com.turskyi.gallery.fragments.BottomNavigationFragment

/** Gallery with three bottom navigation tabs: pictures, folders and random online photos */
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeFragment: BottomNavigationFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* permission must in "onCreate" */
        checkPermission()
    }

    private fun checkPermission() {
        val permissionGranted =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        if (permissionGranted != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
        } else {
            showFragment()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray(),
            PERMISSION_EXTERNAL_STORAGE
        )
    }

    private fun showFragment() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        homeFragment = BottomNavigationFragment()
        homeFragment.arguments = intent.extras
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.add(R.id.frameLayout, homeFragment).commit()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResult: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResult)
        when (requestCode) {
            PERMISSION_EXTERNAL_STORAGE -> {

                /** If request is cancelled, the result array is empty. */
                if ((grantResult.isNotEmpty()
                            && grantResult[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    binding.getPermissionView.visibility = View.GONE
                    showFragment()
                } else {
                    /** shows the "get permission view" */
                    binding.getPermissionView.visibility = VISIBLE
                    binding.getPermissionView.setOnClickListener {
                        requestPermission()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {

        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.really_exit))
                .setMessage(getString(R.string.are_you_sure_you_want_to_exit))
                .setNegativeButton(getString(R.string.home_cancel), null)
                .setPositiveButton(getString(R.string.home_ok)) { _, _ ->
                    super@HomeActivity.onBackPressed()
                }
                .create().show()
        } else {
            homeFragment.setVisibility(VISIBLE)
            supportFragmentManager.popBackStack()
        }
    }
}
