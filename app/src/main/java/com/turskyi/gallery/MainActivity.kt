package com.turskyi.gallery

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.adapters.GridRecyclerAdapter
import com.turskyi.gallery.adapters.ListRecyclerAdapter
import com.turskyi.gallery.adapters.MyRecyclerViewAdapter
import com.turskyi.gallery.model.MyFile
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private var path = "/storage/"

    private var quantityOfColumns: Int = 1

    private lateinit var aRecyclerView: RecyclerView

    // My File list
    private var aFileList = ArrayList<MyFile?>()

    private var maxRow = 10

    private var isLoading = false
    private lateinit var mAdapter: MyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPermission()

        aRecyclerView = findViewById(R.id.recycler_view)

//        if (quantityOfColumns == 1)
        aRecyclerView.layoutManager = LinearLayoutManager(this)
//        else
//            aRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        btn_arrow_back.setOnClickListener {
            onBackPressed()
        }

        FileLiveSingleton.getInstance().getPath().observe(this, Observer<String> { path ->
            if (path != null && path.isNotEmpty()) {
                maxRow = 10
                this.path = path
                readFiles()
            }
        })

        btn_view_changer.setOnClickListener (firstButtonListener)

        getNumberOfColumns()

        aRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!isLoading) {
                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == aFileList.size - 2) {
                        maxRow += 10
                        isLoading = true
                        loadMore()
                    }
                }
            }
        })

        // mAdapter.getAllChecked()
    }

    private fun loadMore() {
        val handler = Handler()

//        handler.post {
//            aFileList.add(null)
//            mAdapter.notifyItemChanged(aFileList.size - 1)
//        }

        val task = Runnable {
            aFileList.removeAt(aFileList.size - 1)
            val scrollPosition = aFileList.size
            mAdapter.notifyItemRemoved(scrollPosition)

            val currentSize = scrollPosition
            val nextLimit = currentSize + 10

            val myList = ArrayList<MyFile?>()

            val f = File(path)
            val files = f.listFiles()

            for (inFile in files) {
                if (inFile.path == "/storage/self") continue
                else if (inFile.path == "/storage/emulated") {
                    if (inFile.isDirectory) {
                        aFileList.add(MyFile("/storage/emulated/0/", inFile.name, null, null))
                    }
                }
            }

            for (index in files.indices) {
                // Skip old files
                if (index < currentSize) continue

                // break for maxLimit
                if (nextLimit == index) break

                if (files[index].isDirectory) {

                    var imageFile: MyFile? = null

                    val filesInDirectory = files[index].listFiles()

                    if (filesInDirectory != null && filesInDirectory.isNotEmpty()) {
                        for (mFile in filesInDirectory) {
                            if (mFile.extension in listOf("jpeg", "png", "jpg", "webp", "JPEG", "PNG", "JPG")) {
                                imageFile = MyFile("${mFile.path}/", mFile.name, mFile.extension, null)
                                break
                            }
                        }
                    }

                    myList.add(MyFile("${files[index].path}/", files[index].name, null, imageFile))
                } else if (files[index].extension in listOf("jpeg", "png", "jpg", "webp", "JPEG", "PNG", "JPG")) {
                    myList.add(MyFile("${files[index].absolutePath}/", files[index].name, files[index].extension, null))
                }

            }
            mAdapter.addNewFiles(myList)
            isLoading = false
        }

        /// postDelayed for loading simulation with sleep thread
        handler.postDelayed(task, 0)
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
        aFileList = ArrayList()

        toolbar_title.text = path
        btn_arrow_back.visibility = VISIBLE
        if (path.endsWith("/storage/")) {
            toolbar_title.text = title
            btn_arrow_back.visibility = INVISIBLE
        }

        val f = File(path)

        val files = f.listFiles()

        for (inFile in files) {
            if (inFile.path == "/storage/self") continue
            else if (inFile.path == "/storage/emulated") {
                if (inFile.isDirectory) {
                    aFileList.add(MyFile("/storage/emulated/0/", inFile.name, null, null))
                }
            }
        }

        for (index in files.indices) {
            if (maxRow == index) break

            // This variable for image
            var imageFile: MyFile? = null
                if (files[index].isDirectory) {

                    /// Get List of files in folder
                    val filesInDirectory = files[index].listFiles()

                    // Search for a photo
                    if (filesInDirectory != null && filesInDirectory.isNotEmpty()) {
                        for (aFile in filesInDirectory) {
                            if (aFile.extension in listOf("jpeg", "png", "jpg", "webp", "JPEG", "PNG", "JPG")) {
                                imageFile = MyFile("${aFile.path}/", aFile.name, aFile.extension, null)
                                break
                            }
                        }
                    }
                    else {

                    // Skip folder if it is empty
                    continue
                }
                    aFileList.add(MyFile("${files[index].path}/", files[index].name, null, imageFile))
                } else if (files[index].extension in listOf("jpeg", "png", "jpg", "webp", "JPEG", "PNG", "JPG")) {
                    aFileList.add(
                        MyFile(
                            "${files[index].absolutePath}/",
                            files[index].name,
                            files[index].extension,
                            null
                        )
                    )
            }
        }

        mAdapter = MyRecyclerViewAdapter(aFileList)

        recycler_view.adapter = mAdapter

        if (quantityOfColumns == 1)
            recycler_view.adapter = ListRecyclerAdapter(this, aFileList)
        else
            recycler_view.adapter = GridRecyclerAdapter(this, aFileList)
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
