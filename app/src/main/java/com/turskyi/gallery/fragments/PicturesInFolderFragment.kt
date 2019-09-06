package com.turskyi.gallery.fragments

import android.annotation.SuppressLint
import android.content.ContentUris
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.turskyi.gallery.R
import com.turskyi.gallery.adapters.PictureInFolderListAdapter
import com.turskyi.gallery.adapters.PictureInFolderStaggeredAdapter
import com.turskyi.gallery.controllers.MainThreadExecutor
import com.turskyi.gallery.controllers.PicturesInFolderPositionalDataSource
import com.turskyi.gallery.interfaces.IOnBackPressed
import com.turskyi.gallery.interfaces.OnPictureClickListener
import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.GalleryPicture
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewmodels.PicturesInFolderViewModel
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_pictures.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.util.concurrent.Executors

class PicturesInFolderFragment(private val galleryFolder: GalleryFolder?) :
    Fragment(R.layout.fragment_pictures),
    OnPictureClickListener,
    IOnBackPressed {

    private lateinit var picturesInFolderViewModel: PicturesInFolderViewModel
    private lateinit var staggeredViewAdapter: PictureInFolderStaggeredAdapter
    private lateinit var listViewAdapter: PictureInFolderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        picturesInFolderViewModel =
            ViewModelProvider(activity!!).get(PicturesInFolderViewModel::class.java)
    }

    @SuppressLint("WrongThread")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateFragment()
    }

    private fun updateFragment() {
        btnArrowBack.setOnClickListener {
            onBackPressed()
        }

        val dataSource = PicturesInFolderPositionalDataSource(
            activity?.applicationContext!!,
            galleryFolder?.folderPath!!
        )

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()

        val pagedList: PagedList<GalleryPicture> = PagedList.Builder(dataSource, config)
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .setNotifyExecutor(MainThreadExecutor())
            .build()

        picturesInFolderViewModel.viewTypes.observe(this, Observer { viewType ->
            when (viewType) {
                ViewType.DELETE -> {
                    btnViewChanger.setImageResource(R.drawable.ic_remove32)
                }
                ViewType.STAGGERED -> {
                    picturesInFolderViewModel.staggeredGridLayoutManager?.spanCount = 2
                    btnViewChanger.setImageResource(R.drawable.ic_view_list_white)
                }
                else -> {
                    picturesInFolderViewModel.staggeredGridLayoutManager?.spanCount = 1
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
                    recyclerView.adapter = listViewAdapter
                    staggeredViewAdapter.changeViewType()
                }
                picturesInFolderViewModel.viewTypes.value == ViewType.LINEAR -> {
                    picturesInFolderViewModel.setViewType(ViewType.STAGGERED)
                    staggeredViewAdapter.submitList(pagedList)
                    recyclerView.adapter = staggeredViewAdapter
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

        checkIfListEmpty(pagedList)

        updateLayoutManager()

        recyclerView.adapter = staggeredViewAdapter
    }

    private fun checkIfListEmpty(pagedList: PagedList<GalleryPicture>) {
        val fragmentActivity: FragmentActivity? = activity
        if (pagedList.size < 1) {
            onBackPressed()
        } else {
            fragmentActivity?.bottomNavigationView?.visibility = View.VISIBLE
        }
    }

    override fun addOnLongClick(galleryPicture: GalleryPicture) {
        picturesInFolderViewModel.selectedPictures.add(galleryPicture)
        picturesInFolderViewModel.changeLayoutView()
    }

    override fun removeOnLongClick(galleryPicture: GalleryPicture, viewType: ViewType) {
        picturesInFolderViewModel.changeLayoutView()
        picturesInFolderViewModel.selectedPictures.remove(galleryPicture)
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
        picturesInFolderViewModel.staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        picturesInFolderViewModel.viewTypes.value = ViewType.STAGGERED
        recyclerView.layoutManager = picturesInFolderViewModel.staggeredGridLayoutManager
    }

    override fun onBackPressed() {
        // TODO: Why  when I change layout to ListView, then press click forward (on folder or image)
        //  and then press back listView become grid?
        fragmentManager?.popBackStack()
    }
}