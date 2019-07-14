package com.turskyi.gallery

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var aFolder: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        aFolder = findViewById(R.id.image_view)
        val aBundle:Bundle? = intent.extras
        if (aBundle != null) {
            aFolder.setImageResource(aBundle.getInt("Image"))
        }
    }
}