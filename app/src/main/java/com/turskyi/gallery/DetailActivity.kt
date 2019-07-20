package com.turskyi.gallery

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    private lateinit var aFileIV: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        aFileIV = findViewById(R.id.image_view_zoom)
        val aBundle: Bundle? = intent.extras
        if (aBundle != null) {
            val aBitmap = BitmapFactory.decodeFile(aBundle.getString("File"))
            aFileIV.setImageBitmap(aBitmap)
        }
    }
}
