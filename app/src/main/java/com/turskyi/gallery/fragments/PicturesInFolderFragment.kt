package com.turskyi.gallery.fragments

import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.turskyi.gallery.R
import com.turskyi.gallery.adapters.PicturesViewAdapter
import com.turskyi.gallery.models.GalleryPicture
import com.turskyi.gallery.models.ViewTypes
import kotlinx.android.synthetic.main.fragment_pictures.*
import kotlinx.android.synthetic.main.toolbar.*

// I do not use this Fragment because I do not now how to implement it yet,
// so for now I use PicturesInFolderActivity
class PicturesInFolderFragment : Fragment() {

    /** My File list */
    private var allLoaded = false
    private lateinit var viewAdapter: PicturesViewAdapter
    private var layoutViewType: ViewTypes = ViewTypes.GRID
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pictures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnViewChanger.setOnClickListener {
            layoutViewType = if (layoutViewType.id == ViewTypes.LINEAR.id) {
                btnViewChanger.setImageResource(R.drawable.ic_view_list_white)
                ViewTypes.GRID
            } else {
                btnViewChanger.setImageResource(R.drawable.ic_grid)
                ViewTypes.LINEAR
            }

            /** This method updates adapter. */
            viewAdapter.changeViewType()
            context?.let { context -> fetchGalleryImages(context) }
        }

        //switch between listView and gridView
        updateLayoutManager()
        context?.let { fetchGalleryImages(it) }

        //my idea to update view after file has been deleted
//        FileLiveSingleton.getInstance().getUpdatedState().observe(this, Observer<Boolean> { isStateUpdated ->
//            if ( isStateUpdated  == true) {
//                context?.let { getGalleryImages(it) }
//            }
//        })
    }

    private fun fetchGalleryImages(context: Context) {

        /** Create an array of pictures */
        val galleryImageUrls = mutableListOf<GalleryPicture>()

        /** Get all columns of files with image type */
        val columns = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID
        )

        /** order data by date */
        val orderBy = MediaStore.Images.Media.DATE_TAKEN

        /** This cursor will hold the result of the query */
        val cursor = context.contentResolver.query(

            /** get all data in Cursor by sorting in descending order */
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
            null, "$orderBy DESC"
        )

        if (cursor != null && !allLoaded) {
            var index = 0
            while (cursor.moveToPosition(index)) {
                val pictureArray = cursor.getString(index).split('/')
                galleryImageUrls.add(GalleryPicture(cursor.getString(index)))
                index++
            }
            cursor.close()

            /** switch between two viewHolders */
            viewAdapter = PicturesViewAdapter(galleryImageUrls)

            /** Without this line nothing gonna shows up */
            recyclerView.adapter = viewAdapter
        }
    }

    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.INVISIBLE
        staggeredGridLayoutManager =
            if (layoutViewType == ViewTypes.GRID) StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            else StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
    }

    /* I will change this method later to make an animation effect */
    private fun updateAnimation() {
        if (layoutViewType == ViewTypes.STAGGERED) staggeredGridLayoutManager?.spanCount = 2
        else staggeredGridLayoutManager?.spanCount = 1
        viewAdapter.notifyItemRangeChanged(0, viewAdapter.itemCount)
    }
}