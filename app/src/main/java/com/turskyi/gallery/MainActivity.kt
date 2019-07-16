package com.turskyi.gallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.adapter.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    private var path = "/storage/emulated/0/"

    private var quantityOfColumns: Int = 1

    lateinit var aRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val path = intent.getStringExtra("path")
        if (path != null) {
            this.path = path
        }

        increment_columns.setOnClickListener {
            incrementColumns()
        }

        decrement_columns.setOnClickListener {
            decrementColumns()
        }

        aRecyclerView = findViewById(R.id.recycler_view)

        getNumberOfColumns()

        getPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
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

    private fun readFiles() {
        val fileList: ArrayList<File> = ArrayList()

        val f = File(path)

        val files = f.listFiles()

        for (inFile in files) {
            if (inFile.isDirectory) {
                fileList.add(File("${inFile.path}/", inFile.name))
            }
        }

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
    }

    /**
     * This method is called when the minus button is clicked.
     */
    private fun decrementColumns() {
        if (quantityOfColumns == 1) {
            return
        }
        quantityOfColumns -= 1
        displayNumberOfColumns(quantityOfColumns)
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
}
