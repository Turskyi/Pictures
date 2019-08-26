package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.turskyi.gallery.R
import com.turskyi.gallery.R.drawable.ic_view_list_white
import com.turskyi.gallery.adapters.FoldersViewAdapter
import com.turskyi.gallery.interfaces.OnFolderClickListener
import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewmodels.FoldersViewModel
import kotlinx.android.synthetic.main.fragment_folders.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

class FoldersFragment : Fragment(), OnFolderClickListener {

    //TODO в активності тільки те, що безпосередньо потрібне для відображення View
    //TODO не інформативна назва змінної, чого Enum і як це стосується її функції
   private lateinit var foldersViewModel: FoldersViewModel
    private lateinit var viewAdapter: FoldersViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        foldersViewModel = ViewModelProvider(this).get(FoldersViewModel::class.java)
        return inflater.inflate(com.turskyi.gallery.R.layout.fragment_folders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foldersViewModel.viewTypes.observe(this, Observer { viewType ->
            when (viewType) {
                ViewType.DELETE -> {
                    updateBtnViewChanger(viewType)
                }
                ViewType.GRID -> {
                    updateBtnViewChanger(viewType)


                    viewAdapter.changeViewType()
                }
                else -> {
                    updateBtnViewChanger(viewType)
                    viewAdapter.changeViewType()
                }
            }
        })

        btnViewChanger.setOnClickListener {

            when {
                foldersViewModel.selectedFolders.size > 0 -> deleteAllSelected()
                foldersViewModel.currentViewType.id == ViewType.LINEAR.id -> {
                    viewAdapter.changeViewType()
                    btnViewChanger.setImageResource(R.drawable.ic_view_list_white)
                    foldersViewModel.currentViewType = ViewType.GRID
                }
                else -> {
                    viewAdapter.changeViewType()
                    btnViewChanger.setImageResource(R.drawable.ic_grid)
                    foldersViewModel.currentViewType = ViewType.LINEAR
                }
            }
            updateAnimation()
        }

        /** get number of columns and switch between listView and gridView */
        updateLayoutManager()

        /* switch between two viewHolders */
        viewAdapter = FoldersViewAdapter(foldersViewModel.listOfFolders, this)
        /* Without this line nothing gonna shows up */
        recyclerView.adapter = viewAdapter
    }

    override fun addOnLongClick(galleryFolder: GalleryFolder) {
        foldersViewModel.selectedFolders.add(galleryFolder)
foldersViewModel.changeLayoutView()
    }

    override fun removeOnLongClick(galleryFolder: GalleryFolder, viewType: ViewType) {
      foldersViewModel.changeLayoutView()
        foldersViewModel.selectedFolders.remove(galleryFolder)
if (foldersViewModel.selectedFolders.isEmpty()) {
    updateBtnViewChanger(viewType)
}
    }

    private fun deleteAllSelected() {
        foldersViewModel.selectedFolders.let {
            for (selectedFolder in it) {
                val file = File(selectedFolder.folderPath)
                if (file.exists())
                    file.delete()
            }
        }
    }

    private fun updateBtnViewChanger(viewType: ViewType) {
        when (viewType) {
            ViewType.DELETE -> {
                btnViewChanger.setImageResource(R.drawable.ic_remove32)
            }
            ViewType.GRID -> {
                btnViewChanger.setImageResource(ic_view_list_white)
            }
            else -> {
                btnViewChanger.setImageResource(R.drawable.ic_grid)
            }
        }
    }

    // this method is absolutely the same as  in the "PicturesFragment" I consider about
    // combining them somewhere, for example in "ToolbarFragment"
    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.INVISIBLE
        foldersViewModel.gridLayoutManager =
            if (foldersViewModel.currentViewType == ViewType.GRID)
            GridLayoutManager(context, 2)
        else GridLayoutManager(context, 1)
        recyclerView.layoutManager = foldersViewModel.gridLayoutManager
    }

    /* I will use the following method later to make an animation effect */
    private fun updateAnimation() {
        if (foldersViewModel.currentViewType == ViewType.GRID)
            foldersViewModel.gridLayoutManager?.spanCount = 2
        else foldersViewModel.gridLayoutManager?.spanCount = 1
        viewAdapter.notifyItemRangeChanged(0, viewAdapter.itemCount)
    }
}
