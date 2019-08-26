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
import com.turskyi.gallery.R.layout
import com.turskyi.gallery.adapters.PicturesViewAdapter
import com.turskyi.gallery.interfaces.OnPictureClickListener
import com.turskyi.gallery.models.GalleryPicture
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewmodels.PicturesViewModel
import kotlinx.android.synthetic.main.fragment_pictures.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

class PicturesFragment :
    Fragment(),
    OnPictureClickListener {

    //TODO в активності тільки те, що безпосередньо потрібне для відображення View
    //TODO не інформативна назва змінної, чого Enum і як це стосується її функції
    private lateinit var picturesViewModel: PicturesViewModel
    private lateinit var viewAdapter: PicturesViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        picturesViewModel = ViewModelProvider(this).get(PicturesViewModel::class.java)
        return inflater.inflate(layout.fragment_pictures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        picturesViewModel.viewTypes.observe(this, Observer { viewType ->
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
            //TODO: should I move this block of code to VM?
            when {
                picturesViewModel.selectedPictures.size > 0 -> deleteAllSelected()
                picturesViewModel.currentViewType.id == ViewType.LINEAR.id -> {
                    viewAdapter.changeViewType()
                        btnViewChanger.setImageResource(ic_view_list_white)
                    picturesViewModel.currentViewType = ViewType.GRID
                }
                else -> {
                    viewAdapter.changeViewType()
                       btnViewChanger.setImageResource(R.drawable.ic_grid)
                    picturesViewModel.currentViewType = ViewType.LINEAR
                }
            }
            updateAnimation()
        }

        /** get number of columns and switch between listView and gridView */
        updateLayoutManager()

        viewAdapter = PicturesViewAdapter(picturesViewModel.listOfPictures, this)
        /* Without this line nothing going to show up */
        recyclerView.adapter = viewAdapter
    }

    override fun addOnLongClick(galleryPicture: GalleryPicture) {
        picturesViewModel.selectedPictures.add(galleryPicture)
        picturesViewModel.changeLayoutView()
    }

    override fun removeOnLongClick(galleryPicture: GalleryPicture, viewType: ViewType) {
        picturesViewModel.changeLayoutView()
        picturesViewModel.selectedPictures.remove(galleryPicture)
        if (picturesViewModel.selectedPictures.isEmpty()) {
            updateBtnViewChanger(viewType)
        }
    }

    private fun deleteAllSelected() {
        picturesViewModel.selectedPictures.let {
            for (selectedPicture in it) {
                val file = File(selectedPicture.path)
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

    // this method is absolutely the same as  in the "FoldersFragment" I consider about
    // combining them somewhere, for example in "ToolbarFragment"
    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.INVISIBLE
        picturesViewModel.gridLayoutManager =
            if (picturesViewModel.currentViewType == ViewType.GRID)
                GridLayoutManager(context, 2)
            else GridLayoutManager(context, 1)
        recyclerView.layoutManager = picturesViewModel.gridLayoutManager
    }

    /* I am going to use this method later to make an animation effect.
     For now if I do not use "changeViewType()" I will have animation, but loose listview */
    private fun updateAnimation() {
        if (picturesViewModel.currentViewType == ViewType.GRID)
            picturesViewModel.gridLayoutManager?.spanCount = 2
        else picturesViewModel.gridLayoutManager?.spanCount = 1
        viewAdapter.notifyItemRangeChanged(0, viewAdapter.itemCount)
    }
}
