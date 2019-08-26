package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.turskyi.gallery.R
import com.turskyi.gallery.adapters.PicturesInFolderViewAdapter
import com.turskyi.gallery.interfaces.IOnBackPressed
import com.turskyi.gallery.interfaces.OnPictureClickListener
import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.GalleryPicture
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewmodels.PicturesInFolderViewModel
import kotlinx.android.synthetic.main.fragment_pictures.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

class PicturesInFolderFragment(private val galleryFolder: GalleryFolder?) :
    Fragment(),
    OnPictureClickListener,
    IOnBackPressed {

    //TODO в активності тільки те, що безпосередньо потрібне для відображення View
    //TODO не інформативна назва змінної, чого Enum і як це стосується її функції
    private lateinit var picturesInFolderViewModel: PicturesInFolderViewModel
    private lateinit var viewAdapter: PicturesInFolderViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        picturesInFolderViewModel =
            ViewModelProvider(this).get(PicturesInFolderViewModel::class.java)
        return inflater.inflate(com.turskyi.gallery.R.layout.fragment_pictures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnArrowBack.setOnClickListener {
            onBackPressed()
        }

        val listOfPicturesInFolder = galleryFolder?.images

        picturesInFolderViewModel.viewTypes.observe(this, Observer { viewType ->
            when (viewType) {
                ViewType.DELETE -> {
                    updateBtnViewChanger(viewType)
                }
                ViewType.STAGGERED -> {
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
                picturesInFolderViewModel.selectedPictures.size > 0 -> deleteAllSelected()
                picturesInFolderViewModel.currentViewType.id == ViewType.LINEAR.id -> {
                    viewAdapter.changeViewType()
                    btnViewChanger.setImageResource(R.drawable.ic_view_list_white)
                    picturesInFolderViewModel.currentViewType = ViewType.STAGGERED
                }
                else -> {
                    viewAdapter.changeViewType()
                    btnViewChanger.setImageResource(R.drawable.ic_grid)
                    picturesInFolderViewModel.currentViewType = ViewType.LINEAR
                }
            }
            updateAnimation()
        }

        updateLayoutManager()

        viewAdapter = PicturesInFolderViewAdapter(listOfPicturesInFolder, null)

        recyclerView.adapter = viewAdapter
    }

    override fun addOnLongClick(galleryPicture: GalleryPicture) {
        picturesInFolderViewModel.selectedPictures.add(galleryPicture)
        picturesInFolderViewModel.changeLayoutView()
    }

    override fun removeOnLongClick(galleryPicture: GalleryPicture, viewType: ViewType) {
        picturesInFolderViewModel.changeLayoutView()
        picturesInFolderViewModel.selectedPictures.remove(galleryPicture)
        if (picturesInFolderViewModel.selectedPictures.isEmpty()) {
            updateBtnViewChanger(viewType)
        }
    }

    private fun deleteAllSelected() {
        picturesInFolderViewModel.selectedPictures.let {
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
            ViewType.STAGGERED -> {
                btnViewChanger.setImageResource(R.drawable.ic_view_list_white)
            }
            else -> {
                btnViewChanger.setImageResource(R.drawable.ic_grid)
            }
        }
    }

    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.VISIBLE
        picturesInFolderViewModel.staggeredGridLayoutManager =
            if (picturesInFolderViewModel.currentViewType == ViewType.STAGGERED)
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        else StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = picturesInFolderViewModel.staggeredGridLayoutManager
    }

    /* I will change this method later to make an animation effect */
    private fun updateAnimation() {
        if (picturesInFolderViewModel.currentViewType == ViewType.STAGGERED)
            picturesInFolderViewModel.staggeredGridLayoutManager?.spanCount = 2
        else picturesInFolderViewModel.staggeredGridLayoutManager?.spanCount = 1
        viewAdapter.notifyItemRangeChanged(0, viewAdapter.itemCount)
    }

    override fun onBackPressed() {
        fragmentManager?.popBackStack()
    }
}