package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.turskyi.gallery.FileLiveSingleton
import com.turskyi.gallery.R
import com.turskyi.gallery.adapters.PicturesStaggeredViewAdapter
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.interfaces.OnPictureClickListener
import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.GalleryPicture
import com.turskyi.gallery.models.ViewTypes
import kotlinx.android.synthetic.main.fragment_pictures.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

class PicturesInFolderFragment(private val galleryFolder: GalleryFolder?) : Fragment(), OnPictureClickListener {


    //TODO в активності тільки те, що безпосередньо потрібне для відображення View
    //TODO не інформативна назва змінної, чого Enum і як це стосується її функції
    private var layoutViewType: ViewTypes = ViewTypes.STAGGERED
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private lateinit var viewAdapter: PicturesStaggeredViewAdapter
    private var selectedPictures: MutableList<GalleryPicture>? = mutableListOf()

    override fun addOnClick(galleryPicture: GalleryPicture) {
        selectedPictures?.add(galleryPicture)
//        btnViewChanger.setImageResource(R.drawable.ic_remove32)
        updateBtnVieChanger()
    }

    override fun removeOnClick(galleryPicture: GalleryPicture) {
        selectedPictures?.remove(galleryPicture)
//        if (layoutViewType.id == ViewTypes.LINEAR.id) {
//            btnViewChanger.setImageResource(ic_view_list_white)
//        } else {
//            btnViewChanger.setImageResource(R.drawable.ic_grid)
//        }
        updateBtnVieChanger()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.turskyi.gallery.R.layout.fragment_pictures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfPicturesInFolder = galleryFolder?.images

        btnArrowBack.setOnClickListener {
            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val foldersFragment = FoldersFragment()
            foldersFragment.arguments = (context as AppCompatActivity).intent.extras
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, foldersFragment)
                ?.addToBackStack(null)?.commit()
        }

        btnViewChanger.setOnClickListener {
            when {
                selectedPictures?.size!! > 0 -> deleteAllSelected()
                layoutViewType.id == ViewTypes.LINEAR.id -> {
                    viewAdapter.changeViewType()
                    btnViewChanger.setImageResource(R.drawable.ic_view_list_white)
                    layoutViewType = ViewTypes.GRID
                }
                else -> {
                    viewAdapter.changeViewType()
                    btnViewChanger.setImageResource(R.drawable.ic_grid)
                    ViewTypes.LINEAR
                }
            }

            // something wrong with this method which I wanted to use to replace the code above
//            if (selectedPictures?.size!! > 0) {
//                deleteAllSelected()
//            } else updateBtnVieChanger()

            updateAnimation()
        }

        updateLayoutManager()

        //???
        context?.let { galleryFolder?.images }

        viewAdapter = PicturesStaggeredViewAdapter(listOfPicturesInFolder,null)

        recyclerView.adapter = viewAdapter

        //my idea to update view after file has been deleted,
        // it would be great to find out if it is correct
//        FileLiveSingleton.getInstance().getUpdatedState().observe(this, Observer<Boolean> {
//                isStateUpdated ->
//            if ( isStateUpdated  == true) {
//                context?.let { galleryFolder?.images }
//            }
//        })
    }

    private fun deleteAllSelected() {
        selectedPictures?.let {
            for (selectedPicture in it) {
                val file = File(selectedPicture.path)
                if (file.exists())
                    file.delete()
            }
        }
    }

    private fun updateBtnVieChanger() {
        selectedPictures?.size ?: run {
            //        if (selectedFolders?.isEmpty()!!) {
            if (layoutViewType.id == ViewTypes.LINEAR.id) {
                btnViewChanger.setImageResource(R.drawable.ic_view_list_white)
            } else {
                btnViewChanger.setImageResource(R.drawable.ic_grid)
            }
        }
        btnViewChanger.setImageResource(R.drawable.ic_remove32)
    }

    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.VISIBLE
        staggeredGridLayoutManager = if (layoutViewType == ViewTypes.STAGGERED)
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            else StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
    }

    /* I will change this method later to make an animation effect */
    private fun updateAnimation() {
        if (layoutViewType == ViewTypes.STAGGERED)
            staggeredGridLayoutManager?.spanCount = 2
        else staggeredGridLayoutManager?.spanCount = 1
        viewAdapter.notifyItemRangeChanged(0, viewAdapter.itemCount)
    }
}