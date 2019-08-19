package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.turskyi.gallery.R.drawable.ic_grid
import com.turskyi.gallery.R.drawable.ic_view_list_white
import com.turskyi.gallery.R.layout
import com.turskyi.gallery.adapters.PicturesViewAdapter
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.models.ViewTypes
import kotlinx.android.synthetic.main.fragment_pictures.*
import kotlinx.android.synthetic.main.toolbar.*

class PicturesFragment : Fragment() {

    private lateinit var viewAdapter: PicturesViewAdapter
    private var layoutViewType: ViewTypes = ViewTypes.GRID
    private var gridLayoutManager: GridLayoutManager? = null
    private val repository = FilesRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout.fragment_pictures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         val listOfPictures =  repository.getGalleryImages(activity?.applicationContext!!)

        btnViewChanger.setOnClickListener {
            layoutViewType = if (layoutViewType.id != ViewTypes.LINEAR.id) {
                viewAdapter.changeViewType()
                btnViewChanger.setImageResource(ic_grid)
                ViewTypes.LINEAR
            } else {
                viewAdapter.changeViewType()
                btnViewChanger.setImageResource(ic_view_list_white)
                ViewTypes.GRID
            }
            updateAnimation()
        }

        /** switch between listView and gridView */
        updateLayoutManager()
        context?.let { repository.getGalleryImages(it) }

        viewAdapter = PicturesViewAdapter(listOfPictures)

        /* Without this line nothing gonna shows up */
        recyclerView.adapter = viewAdapter
    }

    // this method is absolutely the same as  in the "FoldersFragment" I consider about
    // combining them somewhere, for example in "ToolbarFragment"
    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.INVISIBLE
        gridLayoutManager =
            if (layoutViewType == ViewTypes.GRID)
            GridLayoutManager(context, 2)
        else GridLayoutManager(context, 1)
        recyclerView.layoutManager = gridLayoutManager
    }

    /* I am going to use this method later to make an animation effect.
     For now if I do not use "changeViewType()" I will have animation, but loose listview */
    private fun updateAnimation() {
        if (layoutViewType == ViewTypes.GRID)
            gridLayoutManager?.spanCount = 2
        else gridLayoutManager?.spanCount = 1
        viewAdapter.notifyItemRangeChanged(0, viewAdapter.itemCount)
    }
}
