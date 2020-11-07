package com.turskyi.gallery.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.turskyi.gallery.R
import com.turskyi.gallery.R.drawable.ic_view_list_white
import com.turskyi.gallery.adapters.PictureGridAdapter
import com.turskyi.gallery.adapters.PictureListAdapter
import com.turskyi.gallery.data.GalleryConstants
import com.turskyi.gallery.interfaces.OnPictureLongClickListener
import com.turskyi.gallery.models.PictureUri
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewmodels.PicturesViewModel
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_pictures.*
import kotlinx.android.synthetic.main.toolbar.*

class PicturesFragment : Fragment(R.layout.fragment_pictures), OnPictureLongClickListener {

    private lateinit var gridViewAdapter: PictureGridAdapter
    private lateinit var listViewAdapter: PictureListAdapter
    private lateinit var picturesViewModel: PicturesViewModel
    private var gridLayoutManager: GridLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        picturesViewModel = ViewModelProvider(requireActivity()).get(PicturesViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateFragment()
    }

    private fun updateFragment() {

        picturesViewModel.viewTypes.observe(viewLifecycleOwner, Observer { viewType ->
            when (viewType) {
                ViewType.DELETE -> btnViewChanger.setImageResource(R.drawable.ic_remove32)
                ViewType.GRID -> {
                    gridLayoutManager?.spanCount = 2
                    btnViewChanger.setImageResource(ic_view_list_white)
                }
                else -> {
                    gridLayoutManager?.spanCount = 1
                    btnViewChanger.setImageResource(R.drawable.ic_grid)
                }
            }
        })

        btnViewChanger.setOnClickListener {
            when {
                picturesViewModel.selectedPictures.size > 0 -> deleteAllSelected()
                picturesViewModel.viewTypes.value == ViewType.GRID -> {
                    picturesViewModel.setViewType(ViewType.LINEAR)
                    listViewAdapter.submitList(picturesViewModel.pagedList)
                    picturesRecyclerView.adapter = listViewAdapter
                    /*this method cannot be in "picturesViewModel.viewTypes.observe"
                     because then it changes layout after delete */
                    gridViewAdapter.changeViewType()
                }
                picturesViewModel.viewTypes.value == ViewType.LINEAR -> {
                    picturesViewModel.setViewType(ViewType.GRID)
                    gridViewAdapter.submitList(picturesViewModel.pagedList)
                    picturesRecyclerView.adapter = gridViewAdapter
                    gridViewAdapter.changeViewType()
                }
            }
        }

        gridViewAdapter = PictureGridAdapter(this)
        listViewAdapter = PictureListAdapter(this)

        gridViewAdapter.submitList(picturesViewModel.pagedList)
        listViewAdapter.submitList(picturesViewModel.pagedList)

        checkIfListEmpty(picturesViewModel.pagedList)

        /**
         *@Description gets number of columns and switch between listView and gridView
         * */
        updateLayoutManager()

    }

    private fun checkIfListEmpty(pagedList: PagedList<PictureUri>) {
        val fragmentActivity: FragmentActivity? = activity
        if (pagedList.size < 1) {
            setUpAnEmptyViewImage(fragmentActivity)
        } else {
            fragmentActivity?.bottomNavigationView?.visibility = View.VISIBLE
        }
    }

    override fun addOnLongClick(pictureUri: PictureUri) {
        picturesViewModel.selectedPictures.add(pictureUri)
        picturesViewModel.updateLayoutView()
    }

    override fun removeOnLongClick(pictureUri: PictureUri, viewType: ViewType) {
        picturesViewModel.updateLayoutView()
        picturesViewModel.selectedPictures.remove(pictureUri)
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
        val url = GalleryConstants.GOOGLE_IMAGES
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun deleteAllSelected() {
        picturesViewModel.selectedPictures.let {
//            for (selectedPicture in it) {
//                val file = File(selectedPicture.path)
//                val id = selectedPicture.id
//                if (file.exists()) {
//                    val deleteUri: Uri = ContentUris
//                        .withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//                    activity?.contentResolver?.delete(deleteUri, null, null)
//                    updateFragment()
//                } else {
//                    Toast.makeText(
//                        context,
//                        getString(R.string.picture_does_not_exist),
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
        }
    }

    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.INVISIBLE
        if (picturesViewModel.viewTypes.value == null
            || picturesViewModel.viewTypes.value == ViewType.GRID
        ) {
            picturesViewModel.viewTypes.value = ViewType.GRID
            gridLayoutManager = GridLayoutManager(context, 2)
            /* Without this line nothing going to show up */
            picturesRecyclerView.adapter = gridViewAdapter
            picturesRecyclerView.layoutManager = gridLayoutManager
        } else if (picturesViewModel.viewTypes.value == ViewType.LINEAR) {
            gridLayoutManager = GridLayoutManager(context, 1)
            /* Without this line nothing going to show up */
            picturesRecyclerView.adapter = listViewAdapter
            picturesRecyclerView.layoutManager = gridLayoutManager
        }
        picturesRecyclerView.layoutManager = gridLayoutManager
    }
}
