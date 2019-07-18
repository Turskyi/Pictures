package com.turskyi.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.adapters.LargeRecyclerAdapter
import com.turskyi.gallery.adapters.RecyclerAdapter
import com.turskyi.gallery.model.MyFile
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private var path = "/storage/emulated/0/"

//    lateinit var quantityOfColumns: Int

    private var quantityOfColumns: Int = 1

    private lateinit var aRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        quantityOfColumns = 1

        getPermission()

        aRecyclerView = findViewById(R.id.recycler_view)

        FileLiveSingleton.getInstance().getPath().observe(this, Observer<String> { path ->
            if (path != null && path.isNotEmpty()) {
                this.path = path
                readFiles()
            }
        })
//        /** First version */
//        val path = intent.getStringExtra("path")
//        if (path != null) {
//            this.path = path
//        }

        increment_columns.setOnClickListener {
            incrementColumns()
        }

        decrement_columns.setOnClickListener {
            decrementColumns()
        }

        getNumberOfColumns()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    readFiles()
                    Toast.makeText(this, "permission allowed", Toast.LENGTH_SHORT).show()
                } else {
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

    /**
     * This method updates adapter.
     */
    private fun readFiles() {
        val fileList: ArrayList<MyFile> = ArrayList()

        val f = File(path)

        val files = f.listFiles()

        for (inFile in files) {
            if (inFile.isDirectory) {
                fileList.add(MyFile("${inFile.path}/", inFile.name, null))
            } else if (inFile.extension in listOf("jpeg", "png","jpg")) {
                fileList.add(MyFile("${inFile.absolutePath}/", inFile.name, inFile.extension))
            }
        }
        if(quantityOfColumns == 1)
        recycler_view.adapter = LargeRecyclerAdapter(this, fileList)
        else
        recycler_view.adapter = RecyclerAdapter(this, fileList)
    }

    private fun getNumberOfColumns() {
        val aGridLayoutManager = GridLayoutManager(this@MainActivity, quantityOfColumns)
        aRecyclerView.layoutManager = aGridLayoutManager
    }

    /**
     * This method is called when the plus button is clicked.
     */
    private fun incrementColumns() {
        if (quantityOfColumns == 10) {
            return
        }
        quantityOfColumns += 1
        displayNumberOfColumns(quantityOfColumns)
        readFiles()
    }

    /**
     * This method is called when the minus button is clicked.
     */
    private fun decrementColumns() {
        if (quantityOfColumns == 1) {
            return
        }
        else
        quantityOfColumns -= 1
        displayNumberOfColumns(quantityOfColumns)
        readFiles()
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private fun displayNumberOfColumns(quantityOfColumns: Int) {
        quantity_text_view.text = "$quantityOfColumns"
        getNumberOfColumns()
    }

    companion object {
        private const val PERMISSION_EXTERNAL_STORAGE = 10001
    }

    override fun onBackPressed() {
        FileLiveSingleton.getInstance().setBackPath()
    }
}
