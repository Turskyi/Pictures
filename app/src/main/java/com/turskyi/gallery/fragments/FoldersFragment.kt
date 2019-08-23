package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.turskyi.gallery.R.drawable
import com.turskyi.gallery.R.drawable.ic_view_list_white
import com.turskyi.gallery.adapters.FoldersViewAdapter
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.interfaces.OnFolderClickListener
import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.ViewTypes
import kotlinx.android.synthetic.main.fragment_folders.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

class FoldersFragment : Fragment(), OnFolderClickListener {

    //TODO в активності тільки те, що безпосередньо потрібне для відображення View
    //TODO не інформативна назва змінної, чого Enum і як це стосується її функції
    private var layoutViewType: ViewTypes = ViewTypes.GRID
    private var gridLayoutManager: GridLayoutManager? = null
    private lateinit var viewAdapter: FoldersViewAdapter
    private val repository = FilesRepository()
    private var selectedFolders: MutableSet<GalleryFolder>? = mutableSetOf()

    override fun addOnClick(galleryFolder: GalleryFolder) {
        selectedFolders?.add(galleryFolder)
//        btnViewChanger.setImageResource(drawable.ic_remove32)
        updateBtnVieChanger()
    }

    override fun removeOnClick(galleryFolder: GalleryFolder) {
        selectedFolders?.remove(galleryFolder)
//        if (layoutViewType.id == ViewTypes.LINEAR.id) {
//            btnViewChanger.setImageResource(ic_view_list_white)
//        } else {
//            btnViewChanger.setImageResource(drawable.ic_grid)
//        }
        updateBtnVieChanger()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.turskyi.gallery.R.layout.fragment_folders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfFolders =
            repository.getGalleryFolders(activity?.applicationContext!!)

        btnViewChanger.setOnClickListener {

            when {
                selectedFolders?.size!! > 0 -> deleteAllSelected()
                layoutViewType.id == ViewTypes.LINEAR.id -> {
                    viewAdapter.changeViewType()
                    btnViewChanger.setImageResource(ic_view_list_white)
                    layoutViewType = ViewTypes.GRID
                }
                else -> {
                    viewAdapter.changeViewType()
                    btnViewChanger.setImageResource(drawable.ic_grid)
                    ViewTypes.LINEAR
                }
            }

            // something wrong with this method which I wanted to use to replace the code above
//            if (selectedFolders?.size!! > 0) {
//                deleteAllSelected()
//            } else updateBtnVieChanger()

            updateAnimation()
        }

        /** get number of columns and switch between listView and gridView */
        updateLayoutManager()

        /* switch between two viewHolders */
        viewAdapter = FoldersViewAdapter(listOfFolders, null)

        /* Without this line nothing gonna shows up */
        recyclerView.adapter = viewAdapter
    }

    private fun deleteAllSelected() {
        selectedFolders?.let {
            for (selectedFolder in it) {
                val file = File(selectedFolder.folderPath)
                if (file.exists())
                    file.delete()
            }
        }
    }

    private fun updateBtnVieChanger() {
        selectedFolders?.size ?: run {
            //        if (selectedFolders?.isEmpty()!!) {
            if (layoutViewType.id == ViewTypes.LINEAR.id) {
                btnViewChanger.setImageResource(ic_view_list_white)
            } else {
                btnViewChanger.setImageResource(drawable.ic_grid)
            }
        }
        btnViewChanger.setImageResource(drawable.ic_remove32)
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
