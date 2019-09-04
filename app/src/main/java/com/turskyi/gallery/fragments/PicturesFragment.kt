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
import com.turskyi.gallery.HomeActivity
import com.turskyi.gallery.R
import com.turskyi.gallery.R.drawable.ic_view_list_white
import com.turskyi.gallery.adapters.PictureGridAdapter
import com.turskyi.gallery.adapters.PictureListAdapter
import com.turskyi.gallery.controllers.MainThreadExecutor
import com.turskyi.gallery.controllers.PicturesPositionalDataSource
import com.turskyi.gallery.data.Constants
import com.turskyi.gallery.interfaces.OnPictureClickListener
import com.turskyi.gallery.models.GalleryPicture
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewmodels.PicturesViewModel
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_pictures.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.util.concurrent.Executors

class PicturesFragment : Fragment(R.layout.fragment_pictures), OnPictureClickListener {

    private lateinit var picturesViewModel: PicturesViewModel
    private lateinit var listViewAdapter: PictureListAdapter
    private lateinit var gridViewAdapter: PictureGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        picturesViewModel = ViewModelProvider(this).get(PicturesViewModel::class.java)
    }

    @SuppressLint("WrongThread")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataSource = PicturesPositionalDataSource(activity?.applicationContext!!)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()

        val pagedList: PagedList<GalleryPicture> = PagedList.Builder(dataSource, config)
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .setNotifyExecutor(MainThreadExecutor())
            .build()

        picturesViewModel.viewTypes.observe(this, Observer { viewType ->
            when (viewType) {
                ViewType.DELETE -> {
                    btnViewChanger.setImageResource(com.turskyi.gallery.R.drawable.ic_remove32)
                }
                ViewType.GRID -> {
                    picturesViewModel.gridLayoutManager?.spanCount = 2
                    btnViewChanger.setImageResource(ic_view_list_white)
                }
                else -> {
                    picturesViewModel.gridLayoutManager?.spanCount = 1
                    btnViewChanger.setImageResource(com.turskyi.gallery.R.drawable.ic_grid)
                }
            }
        })

        btnViewChanger.setOnClickListener {
            when {
                picturesViewModel.selectedPictures.size > 0 -> deleteAllSelected()
                picturesViewModel.viewTypes.value == ViewType.GRID -> {
                    picturesViewModel.setViewType(ViewType.LINEAR)
                    listViewAdapter.submitList(pagedList)
                    recyclerView.adapter = listViewAdapter
                    gridViewAdapter.changeViewType()
                }
                picturesViewModel.viewTypes.value == ViewType.LINEAR -> {
                    picturesViewModel.setViewType(ViewType.GRID)
                    gridViewAdapter.submitList(pagedList)
                    recyclerView.adapter = gridViewAdapter
                    gridViewAdapter.changeViewType()
                }
            }
        }

        listViewAdapter = PictureListAdapter(picturesViewModel.listOfPictures, this)
        gridViewAdapter = PictureGridAdapter(picturesViewModel.listOfPictures, this)

        gridViewAdapter.submitList(pagedList)

        checkIfListEmpty()

        /** get number of columns and switch between listView and gridView */
        updateLayoutManager()

        /* Without this line nothing going to show up */
        recyclerView.adapter = gridViewAdapter
    }

    private fun checkIfListEmpty() {
        val fragmentActivity: FragmentActivity? = activity
        if (picturesViewModel.listOfPictures.size == 0) {
            setUpAnEmptyViewImage(fragmentActivity)
        } else if (picturesViewModel.listOfPictures.size > 0) {
            fragmentActivity?.bottomNavigationView?.visibility = View.VISIBLE
        }
    }

    override fun addOnLongClick(galleryPicture: GalleryPicture) {
        picturesViewModel.selectedPictures.add(galleryPicture)
        picturesViewModel.updateLayoutView()
    }

    override fun removeOnLongClick(galleryPicture: GalleryPicture, viewType: ViewType) {
        picturesViewModel.updateLayoutView()
        picturesViewModel.selectedPictures.remove(galleryPicture)
        if (picturesViewModel.selectedPictures.isEmpty()) {
            picturesViewModel.viewTypes.value = viewType
        }
    }

    private fun setUpAnEmptyViewImage(fragmentActivity: FragmentActivity?) {
        emptyView.visibility = View.VISIBLE
        fragmentActivity?.bottomNavigationView?.visibility = View.GONE
        btnViewChanger.visibility = View.GONE
        emptyView.setOnClickListener(sendToGoogleImages)
    }

    private var sendToGoogleImages: View.OnClickListener = View.OnClickListener {
        val url = Constants.GOOGLE_IMAGES
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun deleteAllSelected() {
        picturesViewModel.selectedPictures.let {
            for (selectedPicture in it) {
                val file = File(selectedPicture.path)
                val id = selectedPicture.id
                if (file.exists()) {
                    val deleteUri: Uri = ContentUris
                        .withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    activity?.contentResolver?.delete(deleteUri, null, null)
                    updateActivity()
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.file_is_not_exist),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun updateActivity() {
        val newIntent = Intent(context, HomeActivity::class.java)
        activity?.startActivity(newIntent)
    }

    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.INVISIBLE
        picturesViewModel.gridLayoutManager = GridLayoutManager(context, 2)
        picturesViewModel.viewTypes.value = ViewType.GRID
        recyclerView.layoutManager = picturesViewModel.gridLayoutManager
    }
}
