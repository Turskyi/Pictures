package com.turskyi.gallery.fragments

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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
import com.turskyi.gallery.data.GalleryConstants
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.interfaces.OnFolderLongClickListener
import com.turskyi.gallery.models.Folder
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewmodels.FoldersViewModel
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_folders.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.util.concurrent.Executors

class FoldersFragment : Fragment(com.turskyi.gallery.R.layout.fragment_folders),
    OnFolderLongClickListener {
    //TODO в активності тільки те, що безпосередньо потрібне для відображення View
    //done
    //TODO не інформативна назва змінної, чого Enum і як це стосується її функції
    //done
    private lateinit var foldersViewModel: FoldersViewModel
    private lateinit var gridViewAdapter: FolderGridAdapter
    private lateinit var listViewAdapter: FolderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // I cannot move this method to Home activity, when I tried I failed
        foldersViewModel = ViewModelProvider(activity!!).get(FoldersViewModel::class.java)
    }

    // TODO: fix wrong thread
    @SuppressLint("WrongThread")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // I made a function out of this code here because I have to call it again,
        // when I delete the folder
        updateFragment()
    }

    private fun updateFragment() {
        val dataSource = FoldersPositionalDataSource(activity?.applicationContext!!)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()

        val pagedList: PagedList<Folder> = PagedList.Builder(dataSource, config)
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
                    foldersRecyclerView.adapter = listViewAdapter
                    gridViewAdapter.changeViewType()
                }
                foldersViewModel.viewTypes.value == ViewType.LINEAR -> {
                    foldersViewModel.setViewType(ViewType.GRID)
                    gridViewAdapter.submitList(pagedList)
                    foldersRecyclerView.adapter = gridViewAdapter
                    gridViewAdapter.changeViewType()
                }
            }
        }

        gridViewAdapter = FolderGridAdapter(this)
        listViewAdapter = FolderListAdapter(this)

        gridViewAdapter.submitList(pagedList)

        checkIfListEmpty(pagedList)

        /** get number of columns and switch between listView and gridView */
        updateLayoutManager()

        /* Without this line nothing gonna shows up */
        foldersRecyclerView.adapter = gridViewAdapter
    }

    private fun checkIfListEmpty(pagedList: PagedList<Folder>) {
        val fragmentActivity: FragmentActivity? = activity
        if (pagedList.size == 0) {
            setUpAnEmptyViewImage(fragmentActivity)
        } else {
            fragmentActivity?.bottomNavigationView?.visibility = View.VISIBLE
        }
    }

    override fun addOnLongClick(folder: Folder) {
        val repository = FilesRepository()
        foldersViewModel.selectedFolders.add(folder)
//        adds all images, located in the selected folder to the selected pictures
        for (i in activity?.applicationContext?.let {
            repository.getSetOfImagesInFolder(
                it,
                folder.folderPath
            )
        }!!) {
            foldersViewModel.selectedImages.add(i)
        }
        foldersViewModel.updateLayoutView()
    }

    override fun removeOnLongClick(folder: Folder, viewType: ViewType) {
        foldersViewModel.updateLayoutView()
        foldersViewModel.selectedFolders.remove(folder)
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
        val url = GalleryConstants.GOOGLE_IMAGES
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
                                    getString(R.string.picture_does_not_exist),
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
                        getString(com.turskyi.gallery.R.string.folder_does_not_exist),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    //TODO: should I combine this method and absolutely the same in the "FoldersFragment" somewhere
    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.INVISIBLE
        foldersViewModel.gridLayoutManager = GridLayoutManager(context, 2)
        foldersViewModel.viewTypes.value = ViewType.GRID
        foldersRecyclerView.layoutManager = foldersViewModel.gridLayoutManager
    }
}
