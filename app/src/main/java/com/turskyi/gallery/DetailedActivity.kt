package com.turskyi.gallery

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.turskyi.gallery.data.Constants.KEY_WORD_PATH
import com.turskyi.gallery.models.GalleryPicture
import kotlinx.android.synthetic.main.fragment_detailed.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

class DetailedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        btnArrowBack.setOnClickListener {
            onBackPressed()
        }

        btnViewChanger.setImageResource(R.drawable.ic_remove32)

        val path = intent.getStringExtra(KEY_WORD_PATH)
        val file = File(path!!)

        /** This is the method to open the picture in full screen with Glide */
        val uri: Uri = Uri.fromFile(file)
        Glide.with(this)
            .load(uri)
            .into(imageViewEnlarged)

        btnViewChanger.setOnClickListener {
            Toast.makeText(this, "I want to delete this picture", Toast.LENGTH_LONG).show()
            if (file.exists()) {
                val galleryPicture = GalleryPicture(path)
                galleryPicture.uri?.let {

                    // I do not know how to force this method to delete the picture
                    contentResolver.delete(galleryPicture.uri!!, null, null)
                }
                if (file.exists()) {
                    Toast.makeText(this, "File is not deleted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "File deleted", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "File is not exist", Toast.LENGTH_LONG).show()
            }
        }
    }
}
