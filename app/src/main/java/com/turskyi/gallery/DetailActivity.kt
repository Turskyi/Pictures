package com.turskyi.gallery

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
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

        btn_view_changer.setImageResource(R.drawable.ic_remove32)

        aFileIV = findViewById(R.id.image_view_enlarged)
        val aBundle: Bundle? = intent.extras
        if (aBundle != null) {
            val aBitmap = BitmapFactory.decodeFile(aBundle.getString("File"))
            aFileIV.setImageBitmap(aBitmap)
        }

        btn_view_changer.setOnClickListener {
            Toast.makeText(this, "I want to delete this picture", Toast.LENGTH_LONG).show()

//            if (aBundle != null) {
//                val aBitmap = BitmapFactory.decodeFile(aBundle.getString("File"))
//                val file = File(/*???*/ Environment.getExternalStorageDirectory().path,
//                aFileIV.setImageBitmap(aBitmap).toString())
//                if (file.exists()) {
//                    file.delete()
//                }
//            }

        }
    }
}
