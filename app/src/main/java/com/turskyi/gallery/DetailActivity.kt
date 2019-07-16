package com.turskyi.gallery

import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class DetailActivity : AppCompatActivity() {

    private lateinit var path:String

    lateinit var name: String

    private lateinit var aFile: ImageView


    lateinit var aRecyclerView: RecyclerView

    private var quantityOfColumns: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        aFile = findViewById(R.id.image_view_preview)
//        val aBundle:Bundle? = intent.extras
//        if (aBundle != null) {
//            aFile.setImageResource(aBundle.getInt("File"))
//        }

//        name = findViewById(R.id.file_name)
//        val aBundle:Bundle? = intent.extras
//        if (aBundle != null) {
//            name
//        }

        path = "/storage/emulated/0/${getFileName()}"

        val path = intent.getStringExtra("path")
        if (path != null) {
            this.path = path
        }

        increment_columns.setOnClickListener {
            incrementOfColumns()
        }

        decrement_columns.setOnClickListener {
            decrementOfColumns()
        }


        aRecyclerView = findViewById(R.id.recycler_view)

        getNumberOfColumns()

    }

    private fun getNumberOfColumns() {
        val aGridLayoutManager = GridLayoutManager(this, quantityOfColumns)
        aRecyclerView.layoutManager = aGridLayoutManager
    }

    /**
     * This method is called when the plus button is clicked.
     */
    private fun incrementOfColumns() {
        if (quantityOfColumns == 10) {
            return
        }
        quantityOfColumns += 1
        displayQuantityOfColumns(quantityOfColumns)
    }

    /**
     * This method is called when the minus button is clicked.
     */
    private fun decrementOfColumns() {
        if (quantityOfColumns == 1) {
            return
        }
        quantityOfColumns -= 1
        displayQuantityOfColumns(quantityOfColumns)
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private fun displayQuantityOfColumns(quantityOfColumns: Int) {
        quantity_text_view.text = "$quantityOfColumns"
        getNumberOfColumns()
    }

    private fun getFileName(): String {
        val filepath = Environment.getExternalStorageDirectory().getPath()
        val file = File(filepath )
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.getAbsolutePath() + "/"
    }
}