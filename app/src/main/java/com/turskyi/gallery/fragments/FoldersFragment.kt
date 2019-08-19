package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.turskyi.gallery.R
import com.turskyi.gallery.adapters.FoldersViewAdapter
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.models.ViewTypes
import kotlinx.android.synthetic.main.fragment_pictures.*
import kotlinx.android.synthetic.main.toolbar.*

class FoldersFragment : Fragment() {

    //TODO в активності тільки те, що безпосередньо потрібне для відображення View
    //TODO не інформативна назва змінної, чого Enum і як це стосується її функції
    private var layoutViewType: ViewTypes = ViewTypes.GRID
    private var gridLayoutManager: GridLayoutManager? = null
    private lateinit var viewAdapter: FoldersViewAdapter
    private val repository = FilesRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_folders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfFolders = repository.getGalleryFolders(activity?.applicationContext!!)

        btnViewChanger.setOnClickListener {
            layoutViewType = if (layoutViewType.id == ViewTypes.LINEAR.id) {
                viewAdapter.changeViewType()
                btnViewChanger.setImageResource(R.drawable.ic_view_list_white)
                ViewTypes.GRID
            } else {
                viewAdapter.changeViewType()
                btnViewChanger.setImageResource(R.drawable.ic_grid)
                ViewTypes.LINEAR
            }
            updateAnimation()
        }

        /** get number of columns and switch between listView and gridView */
        updateLayoutManager()
        context?.let { repository.getGalleryFolders(it) }

        /* switch between two viewHolders */
        viewAdapter = FoldersViewAdapter(listOfFolders)

        /* Without this line nothing gonna shows up */
        recyclerView.adapter = viewAdapter
    }

    // this method is absolutely the same as  in the "PicturesFragment" I consider about
    // combining them somewhere, for example in "ToolbarFragment"
    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.INVISIBLE
        gridLayoutManager = if (layoutViewType == ViewTypes.GRID)
            GridLayoutManager(context, 2)
         else GridLayoutManager(context, 1)
        recyclerView.layoutManager = gridLayoutManager
    }

    /* I will use the following method later to make an animation effect */
    private fun updateAnimation() {
        if (layoutViewType == ViewTypes.GRID) gridLayoutManager?.spanCount = 2
        else gridLayoutManager?.spanCount = 1
        viewAdapter.notifyItemRangeChanged(0, viewAdapter.itemCount)
    }
}
