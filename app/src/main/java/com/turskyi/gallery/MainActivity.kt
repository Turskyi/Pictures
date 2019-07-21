package com.turskyi.gallery

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.adapters.GridRecyclerAdapter
import com.turskyi.gallery.adapters.ListRecyclerAdapter
import com.turskyi.gallery.model.MyFile
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File


class MainActivity : AppCompatActivity() {

    private var path = "/storage/"

    private var quantityOfColumns: Int = 1

    private lateinit var aRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getPermission()

        btn_arrow_back.setOnClickListener {
            onBackPressed()
        }

        aRecyclerView = findViewById(R.id.recycler_view)

        FileLiveSingleton.getInstance().getPath().observe(this, Observer<String> { path ->
            if (path != null && path.isNotEmpty()) {
                this.path = path
                readFiles()
            }
        })

        btn_view_changer.setOnClickListener (firstButtonListener)

//        btn_folders.setOnClickListener {
//            turnIntoFolders()
//        }
//
//        btn_list.setOnClickListener {
//            turnIntoList()
//        }

        getNumberOfColumns()

    }

    private var firstButtonListener: View.OnClickListener = View.OnClickListener {
        btn_view_changer.setImageResource(R.drawable.ic_view_list_white)
        turnIntoFolders()
        // change the buttonListener
        btn_view_changer.setOnClickListener(secondButtonListener)
    }

    private var secondButtonListener: View.OnClickListener = View.OnClickListener {
        // Find a reference to the button. Change the image.
        btn_view_changer.setImageResource(R.drawable.ic_grid)

        turnIntoList()
        // return first buttonListener
        btn_view_changer.setOnClickListener(firstButtonListener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    readFiles()
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
        toolbar_title.text = path
        btn_arrow_back.visibility = VISIBLE
        if (path.endsWith("/storage/")) {
            toolbar_title.text = title
            btn_arrow_back.visibility = INVISIBLE
        }
        val fileList: ArrayList<MyFile> = ArrayList()

        val f = File(path)

        val files = f.listFiles()

        for (inFile in files) {

            if (inFile.path == "/storage/self") continue
            else if (inFile.path == "/storage/emulated") {
                if (inFile.isDirectory) {
                    fileList.add(MyFile("/storage/emulated/0/", inFile.name, null))
                }
            } else {
                if (inFile.isDirectory) {
                    fileList.add(MyFile("${inFile.path}/", inFile.name, null))

                } else if (inFile.extension in listOf("jpeg", "png", "jpg", "JPG")) {
                    fileList.add(MyFile("${inFile.absolutePath}/", inFile.name, inFile.extension))
                }
            }
        }
        if (quantityOfColumns == 1)
            recycler_view.adapter = ListRecyclerAdapter(this, fileList)
        else
            recycler_view.adapter = GridRecyclerAdapter(this, fileList)
    }

    private fun getNumberOfColumns() {
        val aGridLayoutManager = GridLayoutManager(this@MainActivity, quantityOfColumns)
        aRecyclerView.layoutManager = aGridLayoutManager
    }

    /**
     * This method is called when the folders button is clicked.
     */
    private fun turnIntoFolders() {
        if (quantityOfColumns == 2) {
            return
        }
        quantityOfColumns += 1
        getNumberOfColumns()
        readFiles()
    }

    /**
     * This method is called when the list button is clicked.
     */
    private fun turnIntoList() {
        if (quantityOfColumns == 1) {
            return
        } else
            quantityOfColumns -= 1
        getNumberOfColumns()
        readFiles()
    }

    companion object {
        private const val PERMISSION_EXTERNAL_STORAGE = 10001
    }

    override fun onBackPressed() {
        btn_arrow_back.visibility = VISIBLE
        if (toolbar_title.text == title) {
            AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(
                    android.R.string.yes
                ) { _, _ -> super@MainActivity.onBackPressed() }.create().show()
        } else FileLiveSingleton.getInstance().setBackPath()
    }
}
