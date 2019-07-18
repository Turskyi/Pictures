package com.turskyi.gallery

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.adapters.RecyclerAdapter
import com.turskyi.gallery.model.MyFile
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class DetailActivity : AppCompatActivity() {

    private lateinit var path:String

    lateinit var name: String


    lateinit var aRecyclerView: RecyclerView

    private var quantityOfColumns: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        name = intent.getStringExtra("File")
        path = "/storage/emulated/0/${this.name}"

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

        readFiles()

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
        val filepath = Environment.getDataDirectory().path /*getExternalStorageDirectory().getPath()*/
        val file = File(filepath )
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath + "/"
    }

    private fun readFiles() {
        val fileList: ArrayList<MyFile> = ArrayList()

        val f = File(path)

        val files = f.listFiles()

        for (inFile in files) {
            if (inFile.isDirectory) {
                fileList.add(MyFile("${inFile.path}/", inFile.name, null))
            }
        }

        recycler_view.adapter = RecyclerAdapter(this, fileList)
    }
}