package com.turskyi.gallery.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.FileLiveSingleton
import com.turskyi.gallery.IOnBackPressed
import com.turskyi.gallery.R
import com.turskyi.gallery.adapters.FileRecyclerViewAdapter
import com.turskyi.gallery.models.GalleryPicture
import com.turskyi.gallery.models.MyFile
import com.turskyi.gallery.models.ViewTypes
import com.turskyi.gallery.models.ViewTypes.*
import kotlinx.android.synthetic.main.fragment_pictures.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

class FoldersFragment : Fragment() , IOnBackPressed {
    //TODO в активності тільки те, що безпосередньо потрібне для відображення View
    private var path = "/storage/"

    /** My File list */
        //TODO в активності тільки те, що безпосередньо потрібне для відображення View
    private var aFileList = ArrayList<MyFile?>()

    //TODO не інформативна назва змінної, чого Enum і як це стосується її функції
    private var isGridEnum: ViewTypes = LINEAR

    private var maxRow = 15

    private var isLoading = false
    private lateinit var viewAdapter: FileRecyclerViewAdapter

    /** Create an array of pictures */
    private val galleryPictures = ArrayList<GalleryPicture>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_folders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** switch between two viewHolders */
        viewAdapter = FileRecyclerViewAdapter(requireContext(), aFileList, isGridEnum)

        recyclerView.adapter = viewAdapter

        FileLiveSingleton.getInstance().getPath().observe(this, Observer<String> { path ->
            if (path != null && path.isNotEmpty()) {
                maxRow = 15
                this.path = path
                readFiles()
            }
        })

        btnViewChanger.setOnClickListener(btnToolbarClickListener)

        /** get number of columns */
        updateLayoutManager()

        //TODO я так зрозумів це твоя пагінація, от правильний метод
        // https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/542-urok-14-paging-library-chast-1.html
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        readFiles()

        btnArrowBack.setOnClickListener {
            onBackPressed()
        }
    }

    //TODO так само це має відбуватися в viewmodel
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
                //TODO зроби ці стрінги константами і зберігай в якомусь класі
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
                            //TODO цей ліст теж варто винести, бо часто дублюється. можеш навіть в ресурси
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
            btnViewChanger.setImageResource(R.drawable.ic_view_list_white)
            isGridEnum != GRID
        } else {
            btnViewChanger.setImageResource(R.drawable.ic_grid)
            isGridEnum != GRID
        }

        updateLayoutManager()

        viewAdapter.changeViewType()

        readFiles()
    }

    /** This method updates adapter. */
    private fun readFiles() {

        /* the code bellow doesn't work and I don't know why */
//        if (viewAdapter.getAllChecked().isNotEmpty()){
        //or
//        if (aFileList.contains(MyFile("","","",null,isChecked = true))){
//
//            btnViewChanger.setImageResource(R.drawable.ic_remove32)
//        }

        aFileList = ArrayList()

        //the title of toolbar is the path
//        toolbarTitle.text = path

        btnArrowBack.visibility = VISIBLE
        if (path == "/storage/") {
            btnArrowBack.visibility = INVISIBLE
        }

        val file = File(path)

        //the title is now just a name of a folder without a path except the main screen
        if (path == "/storage/")

        //without this line onBackPress not going to work from the main screen
            toolbarTitle.text = getString(R.string.app_name)
        else
            toolbarTitle.text = file.name

        val files = file.listFiles()

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
                    Log.d("FoldersFragment", "Folder with dot")
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
            if (isGridEnum == GRID) GridLayoutManager(context, 2)
            else GridLayoutManager(context, 1)
        recyclerView.layoutManager = aGridLayoutManager
    }



    override fun onBackPressed() {
        btnArrowBack.visibility = VISIBLE
     return   if (toolbarTitle.text.equals(R.string.app_name)) {
            btnArrowBack.visibility = INVISIBLE
            AlertDialog.Builder(activity?.applicationContext!!)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(
                    android.R.string.yes
                ) { _, _ -> onBackPressed() }.create().show()

     } else
        FileLiveSingleton.getInstance().setBackPath()
    }
}
