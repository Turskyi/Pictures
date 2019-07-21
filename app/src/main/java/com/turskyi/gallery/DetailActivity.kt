package com.turskyi.gallery

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.toolbar.*

class DetailActivity : AppCompatActivity() {
    private lateinit var aFileIV: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        btn_arrow_back.setOnClickListener {
            onBackPressed()
        }

        aFileIV = findViewById(R.id.image_view_zoom)
        val aBundle: Bundle? = intent.extras
        if (aBundle != null) {
            val aBitmap = BitmapFactory.decodeFile(aBundle.getString("File"))
            aFileIV.setImageBitmap(aBitmap)
        }
    }
}
