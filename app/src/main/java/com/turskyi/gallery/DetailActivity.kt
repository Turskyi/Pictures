package com.turskyi.gallery

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
                aFileIV.setImageResource(aBundle.getInt("Image"))
        }
    }
}
