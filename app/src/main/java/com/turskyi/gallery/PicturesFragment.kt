package com.turskyi.gallery

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.adapters.FileRecyclerViewAdapter
import com.turskyi.gallery.models.MyFile
import com.turskyi.gallery.models.ViewTypes
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.util.*

class PicturesFragment : Fragment() {

    private var path = "/storage/"
    private lateinit var viewAdapter: FileRecyclerViewAdapter

    /** My File list */
    private var aFileList = ArrayList<MyFile?>()

    private var isGridEnum: ViewTypes = ViewTypes.LINEAR
    private var isLoading = false
    private var maxRow = 15

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: ViewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.activity_main, container, false
        )

//        val rootView = inflater
//            .inflate(R.layout.activity_main, container, false)

        //switch between two viewHolders
        viewAdapter = FileRecyclerViewAdapter(requireContext(), aFileList, isGridEnum)


        /** Create a list of images */
        FileLiveSingleton.getInstance().getPath().observe(this, Observer<String> { path ->
            if (path != null && path.isNotEmpty()) {
                maxRow = 15
                this.path = path
                readFiles()
            }
        })

        btn_view_changer.setOnClickListener(btnToolbarClickListener)

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
        viewAdapter.getAllChecked()

//        return rootView
        return binding.root
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
                // Skip old files
                if (index < scrollPosition) continue

                // break for maxLimit
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

        /// postDelayed for loading simulation with sleep thread
        handler.postDelayed(task, 0)
    }

    /**
     * This method updates adapter.
     */
    private fun readFiles() {
        aFileList = ArrayList()

        //the title of toolbar is the path
//        toolbar_title.text = path

        btn_arrow_back.visibility = View.VISIBLE
        if (path == "/storage/") {
            btn_arrow_back.visibility = View.INVISIBLE
        }

        val f = File(path)

        //the title is now just a name of a folder without a path except the main screen
        if (path == "/storage/")
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
                /// Get List of files in folder
                val filesInDirectory = files[index].listFiles()

                // Search for a photo
                if (filesInDirectory != null && filesInDirectory.isNotEmpty()) {
                    for (aFile in filesInDirectory) {
                        if (aFile.extension in listOf("jpeg", "png", "jpg", "webp", "JPEG", "PNG", "JPG")) {
                            imageFile = MyFile("${aFile.path}/", aFile.name, aFile.extension, null, false)
                            break
                        }
                    }
                    //doesn't work yet
                } else if (files[index].startsWith(".")) {
                    Log.d("MainActivity", "Folder with dot")
                    continue
                } else {
                    // Skip folder if it is empty
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
        Update the list.
         */
        viewAdapter.setNewList(aFileList)
    }

    private fun updateLayoutManager() {
        val aGridLayoutManager =
            if (isGridEnum == ViewTypes.GRID) GridLayoutManager(context, 2)
            else GridLayoutManager(context, 1)
        recycler_view.layoutManager = aGridLayoutManager
    }

    private var btnToolbarClickListener: View.OnClickListener = View.OnClickListener {
        isGridEnum = when {
            isGridEnum.id == ViewTypes.LINEAR.id -> ViewTypes.GRID
            isLoading -> ViewTypes.LOADING
            else -> ViewTypes.LINEAR
        }

        if (isGridEnum.id == ViewTypes.GRID.id) {
            btn_view_changer.setImageResource(R.drawable.ic_view_list_white)
            isGridEnum != ViewTypes.GRID
        } else {
            btn_view_changer.setImageResource(R.drawable.ic_grid)
            isGridEnum != ViewTypes.GRID
        }

        updateLayoutManager()

        viewAdapter.changeViewType()

        readFiles()
    }
}
