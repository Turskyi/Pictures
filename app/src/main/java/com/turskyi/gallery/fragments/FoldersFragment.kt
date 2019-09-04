package com.turskyi.gallery.fragments

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.turskyi.gallery.R
import com.turskyi.gallery.R.drawable.ic_view_list_white
import com.turskyi.gallery.adapters.FolderGridAdapter
import com.turskyi.gallery.adapters.FolderListAdapter
import com.turskyi.gallery.controllers.FoldersPositionalDataSource
import com.turskyi.gallery.controllers.MainThreadExecutor
import com.turskyi.gallery.data.Constants
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.interfaces.OnFolderClickListener
import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewmodels.FoldersViewModel
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_folders.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.util.concurrent.Executors

class FoldersFragment : Fragment(com.turskyi.gallery.R.layout.fragment_folders),
    OnFolderClickListener {

    //TODO в активності тільки те, що безпосередньо потрібне для відображення View
    //TODO не інформативна назва змінної, чого Enum і як це стосується її функції
    //done
    private lateinit var foldersViewModel: FoldersViewModel
    private lateinit var gridViewAdapter: FolderGridAdapter
    private lateinit var listViewAdapter: FolderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foldersViewModel = ViewModelProvider(this).get(FoldersViewModel::class.java)
    }

    // TODO: fix wrong thread
    @SuppressLint("WrongThread")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataSource = FoldersPositionalDataSource(activity?.applicationContext!!)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()

        val pagedList: PagedList<GalleryFolder> = PagedList.Builder(dataSource, config)
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .setNotifyExecutor(MainThreadExecutor())
            .build()

        foldersViewModel.viewTypes.observe(this, Observer { viewType ->
            when (viewType) {
                ViewType.DELETE -> {
                    btnViewChanger.setImageResource(com.turskyi.gallery.R.drawable.ic_remove32)
                }
                ViewType.GRID -> {
                    foldersViewModel.gridLayoutManager?.spanCount = 2
                    btnViewChanger.setImageResource(ic_view_list_white)
                }
                else -> {
                    foldersViewModel.gridLayoutManager?.spanCount = 1
                    btnViewChanger.setImageResource(com.turskyi.gallery.R.drawable.ic_grid)
                }
            }
        })

        btnViewChanger.setOnClickListener {
            when {
                foldersViewModel.selectedFolders.size > 0 -> deleteAllSelected()
                foldersViewModel.viewTypes.value == ViewType.GRID -> {
                    foldersViewModel.setViewType(ViewType.LINEAR)
                    listViewAdapter.submitList(pagedList)
                    recyclerView.adapter = listViewAdapter
                    gridViewAdapter.changeViewType()
                }
                foldersViewModel.viewTypes.value == ViewType.LINEAR -> {
                    foldersViewModel.setViewType(ViewType.GRID)
                    gridViewAdapter.submitList(pagedList)
                    recyclerView.adapter = gridViewAdapter
                    gridViewAdapter.changeViewType()
                }
            }
        }

        gridViewAdapter = FolderGridAdapter(foldersViewModel.listOfFolders, this)
        listViewAdapter = FolderListAdapter(foldersViewModel.listOfFolders, this)

        gridViewAdapter.submitList(pagedList)

        checkIfListEmpty()

        /** get number of columns and switch between listView and gridView */
        updateLayoutManager()

        /* Without this line nothing gonna shows up */
        recyclerView.adapter = gridViewAdapter
    }

    private fun checkIfListEmpty() {
        val fragmentActivity: FragmentActivity? = activity
        if (foldersViewModel.listOfFolders.size == 0) {
            setUpAnEmptyViewImage(fragmentActivity)
        } else if (foldersViewModel.listOfFolders.size > 0) {
            fragmentActivity?.bottomNavigationView?.visibility = View.VISIBLE
        }
    }

    private val repository = FilesRepository()
    override fun addOnLongClick(galleryFolder: GalleryFolder) {
        foldersViewModel.selectedFolders.add(galleryFolder)
//        add all images, located in the selected folder to the selected pictures
        for (i in activity?.applicationContext?.let { repository.getImagesInFolder(it,galleryFolder.folderPath) }!!) {
            foldersViewModel.selectedImages.add(i)
        }
        foldersViewModel.updateLayoutView()
    }

    override fun removeOnLongClick(galleryFolder: GalleryFolder, viewType: ViewType) {
        foldersViewModel.updateLayoutView()
        foldersViewModel.selectedFolders.remove(galleryFolder)
        if (foldersViewModel.selectedFolders.isEmpty()) {
            foldersViewModel.viewTypes.value = viewType
        }
    }

    private fun setUpAnEmptyViewImage(fragmentActivity: FragmentActivity?) {
        emptyFolders.visibility = View.VISIBLE
        fragmentActivity?.bottomNavigationView?.visibility = View.GONE
        btnViewChanger.visibility = View.GONE
        emptyFolders.setOnClickListener(sendToGoogleImages)
    }

    private var sendToGoogleImages: View.OnClickListener = View.OnClickListener {
        val url = Constants.GOOGLE_IMAGES
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun deleteAllSelected() {
        foldersViewModel.selectedFolders.let {
            for (selectedFolder in it) {
                val folder = File(selectedFolder.folderPath)
                if (folder.exists() && folder.isDirectory) {
                    // delete all pictures in folders
                    foldersViewModel.selectedImages.let {
                        for (selectedPicture in foldersViewModel.selectedImages) {
                            val file = File(selectedPicture.path)
                            val id = selectedPicture.id
                            if (file.exists()) {
                                val deleteUri: Uri = ContentUris
                                    .withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        id
                                    )
                                activity?.contentResolver?.delete(deleteUri, null, null)
                            } else {
                                Toast.makeText(
                                    context,
                                    getString(R.string.file_is_not_exist),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                    //TODO: Delete empty folder
                    folder.delete()
                    updateFragment()
                } else {
                    Toast.makeText(
                        context,
                        getString(com.turskyi.gallery.R.string.file_is_not_exist),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun updateFragment() {
        val fragmentManager: FragmentTransaction =
            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        val foldersFragment = FoldersFragment()
        fragmentManager.replace(com.turskyi.gallery.R.id.container, foldersFragment).commit()
    }

    //TODO: should I combine this method and absolutely the same in the "FoldersFragment" somewhere
    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.INVISIBLE
        foldersViewModel.gridLayoutManager = GridLayoutManager(context, 2)
        foldersViewModel.viewTypes.value = ViewType.GRID
        recyclerView.layoutManager = foldersViewModel.gridLayoutManager
    }
}
