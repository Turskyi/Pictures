package com.turskyi.gallery

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.turskyi.gallery.fragments.HomeFragment
import kotlinx.android.synthetic.main.toolbar.*

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_EXTERNAL_STORAGE = 10001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeFragment = HomeFragment()
        homeFragment.arguments = intent.extras
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.add(android.R.id.content, homeFragment).commit()
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
                    //TODO ти реквестиш пермішн в методі який тригериться коли запрошують пермішн. він не викличиться
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
//    }
}