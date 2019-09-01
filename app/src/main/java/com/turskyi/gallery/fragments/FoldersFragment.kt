package com.turskyi.gallery.fragments

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.turskyi.gallery.interfaces.OnFolderClickListener
import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewmodels.FoldersViewModel
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_folders.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.util.concurrent.Executors

class FoldersFragment : Fragment(), OnFolderClickListener {

    //TODO в активності тільки те, що безпосередньо потрібне для відображення View
    //TODO не інформативна назва змінної, чого Enum і як це стосується її функції
    private lateinit var foldersViewModel: FoldersViewModel
    private lateinit var gridViewAdapter: FolderGridAdapter
    private lateinit var listViewAdapter: FolderListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        foldersViewModel = ViewModelProvider(this).get(FoldersViewModel::class.java)
        return inflater.inflate(com.turskyi.gallery.R.layout.fragment_folders, container, false)
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
                    btnViewChanger.setImageResource(ic_view_list_white)
                }
                else -> {
                    btnViewChanger.setImageResource(com.turskyi.gallery.R.drawable.ic_grid)
                }
            }
        })

        btnViewChanger.setOnClickListener {
            when {
                foldersViewModel.selectedFolders.size > 0 -> deleteAllSelected()
                foldersViewModel.viewTypes.value == ViewType.GRID -> {
                    foldersViewModel.setViewType(ViewType.LINEAR)
                    updateAnimation()
                    gridViewAdapter.submitList(pagedList)
                    gridViewAdapter.changeViewType()
                }
                foldersViewModel.viewTypes.value == ViewType.LINEAR -> {
                    gridViewAdapter = FolderGridAdapter(foldersViewModel.listOfFolders, this)
                    foldersViewModel.setViewType(ViewType.GRID)
                    updateAnimation()
                    gridViewAdapter.submitList(pagedList)
                    recyclerView.adapter = gridViewAdapter
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

    override fun addOnLongClick(galleryFolder: GalleryFolder) {
        foldersViewModel.selectedFolders.add(galleryFolder)
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
                val file = File(selectedFolder.folderPath)

                // id of the first picture in the folder
                val id = selectedFolder.id
                if (file.exists()){
                    //TODO: Delete all pictures in folder
                    /** Deletes a first picture in folder */
                       val deleteUri: Uri = ContentUris
                           .withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                       activity?.contentResolver?.delete(deleteUri, null, null)
                       updateFragment()
                } else {
                    Toast.makeText(context, getString(R.string.file_is_not_exist), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateFragment() {
        val fragmentManager: FragmentTransaction =
            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        val foldersFragment = FoldersFragment()
        fragmentManager.replace(R.id.container, foldersFragment).commit()
    }

    //TODO: should I combine this method and absolutely the same in the "FoldersFragment" somewhere
    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.INVISIBLE
        foldersViewModel.gridLayoutManager = GridLayoutManager(context, 2)
        foldersViewModel.viewTypes.value = ViewType.GRID
        recyclerView.layoutManager = foldersViewModel.gridLayoutManager
    }

    /* I will use the following method later to make an animation effect */
    private fun updateAnimation() {
        if (foldersViewModel.viewTypes.value == ViewType.GRID)
            foldersViewModel.gridLayoutManager?.spanCount = 2
        else foldersViewModel.gridLayoutManager?.spanCount = 1
    }
}
