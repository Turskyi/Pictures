package com.turskyi.gallery.fragments

import android.content.ContentUris
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.turskyi.gallery.R
import com.turskyi.gallery.adapters.PictureInFolderListAdapter
import com.turskyi.gallery.adapters.PictureInFolderStaggeredAdapter
import com.turskyi.gallery.dataSources.PicturesInFolderPositionalDataSource
import com.turskyi.gallery.interfaces.IOnBackPressed
import com.turskyi.gallery.interfaces.OnPictureLongClickListener
import com.turskyi.gallery.models.Folder
import com.turskyi.gallery.models.Picture
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.utils.MainThreadExecutor
import com.turskyi.gallery.viewmodels.PicturesInFolderViewModel
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_pictures.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.util.concurrent.Executors

class PicturesInFolderFragment(private val folder: Folder?) :
    Fragment(R.layout.fragment_pictures),
    OnPictureLongClickListener,
    IOnBackPressed {

    private lateinit var picturesInFolderViewModel: PicturesInFolderViewModel
    private lateinit var staggeredViewAdapter: PictureInFolderStaggeredAdapter
    private lateinit var listViewAdapter: PictureInFolderListAdapter
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        picturesInFolderViewModel =
            ViewModelProvider(activity!!).get(PicturesInFolderViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateFragment()
    }

    private fun updateFragment() {
        btnArrowBack.setOnClickListener {
            onBackPressed()
        }

      //TODO: How to move "dataSource to viewModel" because I cannot get folderPath there.

        val dataSource =
            PicturesInFolderPositionalDataSource(
                activity?.applicationContext!!,
                folder?.folderPath!!
            )

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()

        val pagedList: PagedList<Picture> = PagedList.Builder(dataSource, config)
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .setNotifyExecutor(MainThreadExecutor())
            .build()

        picturesInFolderViewModel.viewTypes.observe(this, Observer { viewType ->
            when (viewType) {
                ViewType.DELETE -> {
                    btnViewChanger.setImageResource(R.drawable.ic_remove32)
                }
                ViewType.STAGGERED -> {
        staggeredGridLayoutManager?.spanCount = 2
                    btnViewChanger.setImageResource(R.drawable.ic_view_list_white)
                }
                else -> {
            staggeredGridLayoutManager?.spanCount = 1
                    btnViewChanger.setImageResource(R.drawable.ic_grid)
                }
            }
        })

        btnViewChanger.setOnClickListener {
            when {
                picturesInFolderViewModel.selectedPictures.size > 0 -> deleteAllSelected()
                picturesInFolderViewModel.viewTypes.value == ViewType.STAGGERED -> {
                    picturesInFolderViewModel.setViewType(ViewType.LINEAR)
                    listViewAdapter.submitList(pagedList)
//                    listViewAdapter.submitList(picturesInFolderViewModel.pagedList)
                    picturesRecyclerView.adapter = listViewAdapter
                    staggeredViewAdapter.changeViewType()
                }
                picturesInFolderViewModel.viewTypes.value == ViewType.LINEAR -> {
                    picturesInFolderViewModel.setViewType(ViewType.STAGGERED)
//                    staggeredViewAdapter.submitList(picturesInFolderViewModel.pagedList)
                    staggeredViewAdapter.submitList(pagedList)
                    picturesRecyclerView.adapter = staggeredViewAdapter
                    staggeredViewAdapter.changeViewType()
                }
            }
        }

        staggeredViewAdapter = context?.let {
            PictureInFolderStaggeredAdapter(this)
        }!!
        listViewAdapter = context?.let {
            PictureInFolderListAdapter(this)
        }!!

        staggeredViewAdapter.submitList(pagedList)
//        staggeredViewAdapter.submitList(picturesInFolderViewModel.pagedList)

        checkIfListEmpty(pagedList)
//        checkIfListEmpty(picturesInFolderViewModel.pagedList)

        updateLayoutManager()

        picturesRecyclerView.adapter = staggeredViewAdapter
    }

    private fun checkIfListEmpty(pagedList: PagedList<Picture>) {
        val fragmentActivity: FragmentActivity? = activity
        if (pagedList.size < 1) {
            onBackPressed()
        } else {
            fragmentActivity?.bottomNavigationView?.visibility = View.VISIBLE
        }
    }

    override fun addOnLongClick(picture: Picture) {
        picturesInFolderViewModel.selectedPictures.add(picture)
        picturesInFolderViewModel.changeLayoutView()
    }

    override fun removeOnLongClick(picture: Picture, viewType: ViewType) {
        picturesInFolderViewModel.changeLayoutView()
        picturesInFolderViewModel.selectedPictures.remove(picture)
        if (picturesInFolderViewModel.selectedPictures.isEmpty()) {
            picturesInFolderViewModel.viewTypes.value = viewType
        }
    }

    private fun deleteAllSelected() {
        picturesInFolderViewModel.selectedPictures.let {
            for (selectedPicture in it) {
                val file = File(selectedPicture.path)
                val id = selectedPicture.id
                if (file.exists()) {
                    val deleteUri: Uri = ContentUris
                        .withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    activity?.contentResolver?.delete(deleteUri, null, null)
                    updateFragment()
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.picture_does_not_exist),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.VISIBLE
     staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        picturesInFolderViewModel.viewTypes.value = ViewType.STAGGERED
        picturesRecyclerView.layoutManager = staggeredGridLayoutManager
    }

    override fun onBackPressed() {
        fragmentManager?.popBackStack()
    }
}