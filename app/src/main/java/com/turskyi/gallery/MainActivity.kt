package com.turskyi.gallery

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.adapters.FileRecyclerViewAdapter
import com.turskyi.gallery.models.MyFile
import com.turskyi.gallery.models.ViewTypes
import com.turskyi.gallery.models.ViewTypes.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private var path = "/storage/"

    /** My File list */
    private var aFileList = ArrayList<MyFile?>()

    private var checkedList = ArrayList<MyFile?>()

    private var isGridEnum: ViewTypes = LINEAR

    private var maxRow = 15

    private var isLoading = false
    private lateinit var viewAdapter: FileRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPermission()

        /** switch between two viewHolders */
        viewAdapter = FileRecyclerViewAdapter(this, aFileList, isGridEnum)

        recycler_view.adapter = viewAdapter

        btn_arrow_back.setOnClickListener {
            onBackPressed()
        }

        FileLiveSingleton.getInstance().getPath().observe(this, Observer<String> { path ->
            if (path != null && path.isNotEmpty()) {
                maxRow = 15
                this.path = path
                readFiles()
            }
        })

        btn_view_changer.setOnClickListener(btnToolbarClickListener)

        /** getNumberOfColumns() replaced with this one bellow */
        updateLayoutManager()

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!isLoading) {
                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == aFileList.size - 2) {
                        maxRow += 15
                        isLoading = true
                        loadMore()
                    }
                }
            }
        })
        viewAdapter.getAllChecked(checkedList)

        /* the code bellow doesn't work and I don't know why */
//        if (checkedList.isNotEmpty()){
//            btn_view_changer.setImageResource(R.drawable.ic_remove32)
//        }
    }

    private fun loadMore() {
        val handler = Handler()

        handler.post {
            aFileList.add(null)
            viewAdapter.notifyItemChanged(aFileList.size - 1)
        }

        val task = Runnable {
            aFileList.removeAt(aFileList.size - 1)
            val scrollPosition = aFileList.size
            viewAdapter.notifyItemRemoved(scrollPosition)

            val nextLimit = scrollPosition + 10

            val myList = ArrayList<MyFile?>()

            val f = File(path)
            val files = f.listFiles()

            for (inFile in files) {
                if (inFile.path == "/storage/self") continue
                else if (inFile.path == "/storage/emulated") {
                    if (inFile.isDirectory) {
                        aFileList.add(
                            MyFile(
                                "/storage/emulated/0/", inFile.name, null, null, false
                            )
                        )
                    }
                }
            }

            for (index in files.indices) {

                /** Skip old files */
                if (index < scrollPosition) continue

                /** break for maxLimit */
                if (nextLimit == index) break

                if (files[index].isDirectory) {

                    var imageFile: MyFile? = null

                    val filesInDirectory = files[index].listFiles()

                    if (filesInDirectory != null && filesInDirectory.isNotEmpty()) {
                        for (mFile in filesInDirectory) {
                            if (mFile.extension in listOf("jpeg", "png", "jpg", "webp", "JPEG", "PNG", "JPG")) {
                                imageFile = MyFile("${mFile.path}/", mFile.name, mFile.extension, null, false)
                                break
                            }
                        }
                    }

                    myList.add(MyFile("${files[index].path}/", files[index].name, null, imageFile, false))
                } else if (files[index].extension in listOf("jpeg", "png", "jpg", "webp", "JPEG", "PNG", "JPG")) {
                    myList.add(
                        MyFile(
                            "${files[index].absolutePath}/",
                            files[index].name,
                            files[index].extension,
                            null,
                            false
                        )
                    )
                }
            }

            viewAdapter.addNewFiles(myList)
            isLoading = false
        }

        /** postDelayed for loading simulation with sleep thread */
        handler.postDelayed(task, 0)
    }

    private var btnToolbarClickListener: View.OnClickListener = View.OnClickListener {
        isGridEnum = when {
            isGridEnum.id == LINEAR.id -> GRID
            isLoading -> LOADING
            else -> LINEAR
        }

        if (isGridEnum.id == GRID.id) {
            btn_view_changer.setImageResource(R.drawable.ic_view_list_white)
            isGridEnum != GRID
        } else {
            btn_view_changer.setImageResource(R.drawable.ic_grid)
            isGridEnum != GRID
        }

        updateLayoutManager()

        viewAdapter.changeViewType()

        readFiles()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_EXTERNAL_STORAGE -> {

                /** If request is cancelled, the result arrays are empty. */
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

    /** This method updates adapter. */
    private fun readFiles() {
        aFileList = ArrayList()

        //the title of toolbar is the path
//        toolbar_title.text = path

        btn_arrow_back.visibility = VISIBLE
        if (path == "/storage/") {
            btn_arrow_back.visibility = INVISIBLE
        }

        val f = File(path)

        //the title is now just a name of a folder without a path except the main screen
        if (path == "/storage/")

        //without this line onBackPress not going to work from the main screen
            toolbar_title.text = title
        else
            toolbar_title.text = f.name

        val files = f.listFiles()

        for (inFile in files) {
            if (inFile.path == "/storage/self") continue
            else if (inFile.path == "/storage/emulated") {
                if (inFile.isDirectory) {
                    aFileList.add(MyFile("/storage/emulated/0/", inFile.name, null, null, false))
                }
            }
        }

        for (index in files.indices) {
            if (maxRow == index) break

            // This variable for image
            var imageFile: MyFile? = null
            if (files[index].isDirectory) {

                /** Get List of files in folder */
                val filesInDirectory = files[index].listFiles()

                // Search for a photo
                if (filesInDirectory != null && filesInDirectory.isNotEmpty()) {
                    for (aFile in filesInDirectory) {
                        if (aFile.extension in listOf("jpeg", "png", "jpg", "webp", "JPEG", "PNG", "JPG")) {
                            imageFile = MyFile("${aFile.path}/", aFile.name, aFile.extension, null, false)
                            break
                        }
                    }

                    /* doesn't work yet */
                } else if (files[index].startsWith(".")) {
                    Log.d("MainActivity", "Folder with dot")
                    continue
//
                } else {
                    /** Skip folder if it is empty */
                    continue
                }
                aFileList.add(MyFile("${files[index].path}/", files[index].name, null, imageFile, false))
            } else if (files[index].extension in listOf("jpeg", "png", "jpg", "webp", "JPEG", "PNG", "JPG")) {
                aFileList.add(
                    MyFile(
                        "${files[index].absolutePath}/",
                        files[index].name,
                        files[index].extension,
                        null, false
                    )
                )
            }


        }
        /**
        This method is called when the list button is clicked.
        This method is called when the folders button is clicked.
        Updates the list
         */
        viewAdapter.setNewList(aFileList)
    }

    private fun updateLayoutManager() {
        val aGridLayoutManager =
            if (isGridEnum == GRID) GridLayoutManager(this@MainActivity, 2)
            else GridLayoutManager(this@MainActivity, 1)
        recycler_view.layoutManager = aGridLayoutManager
    }

    companion object {
        private const val PERMISSION_EXTERNAL_STORAGE = 10001
    }

    override fun onBackPressed() {
        btn_arrow_back.visibility = VISIBLE
        if (toolbar_title.text == title) {
            btn_arrow_back.visibility = INVISIBLE
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
