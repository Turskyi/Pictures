package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.turskyi.gallery.R
import com.turskyi.gallery.R.drawable.ic_view_list_white
import com.turskyi.gallery.R.layout
import com.turskyi.gallery.adapters.PicturesViewAdapter
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.interfaces.OnPictureClickListener
import com.turskyi.gallery.models.GalleryPicture
import com.turskyi.gallery.models.ViewTypes
import kotlinx.android.synthetic.main.fragment_pictures.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

class PicturesFragment : Fragment(), OnPictureClickListener {

    //TODO в активності тільки те, що безпосередньо потрібне для відображення View
    //TODO не інформативна назва змінної, чого Enum і як це стосується її функції
    private var layoutViewType: ViewTypes = ViewTypes.GRID
    private var gridLayoutManager: GridLayoutManager? = null
    private lateinit var viewAdapter: PicturesViewAdapter
    private val repository = FilesRepository()
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
        return inflater.inflate(layout.fragment_pictures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfPictures =
            repository.getGalleryImages(activity?.applicationContext!!)

        btnViewChanger.setOnClickListener {
            when {
                selectedPictures?.size!! > 0 -> deleteAllSelected()
                layoutViewType.id == ViewTypes.LINEAR.id -> {
                    viewAdapter.changeViewType()
                    btnViewChanger.setImageResource(ic_view_list_white)
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

        /** switch between listView and gridView */
        updateLayoutManager()

        viewAdapter = PicturesViewAdapter(listOfPictures, null)

        /* Without this line nothing gonna shows up */
        recyclerView.adapter = viewAdapter

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
                btnViewChanger.setImageResource(ic_view_list_white)
            } else {
                btnViewChanger.setImageResource(R.drawable.ic_grid)
            }
        }
        btnViewChanger.setImageResource(R.drawable.ic_remove32)
    }

    // this method is absolutely the same as  in the "FoldersFragment" I consider about
    // combining them somewhere, for example in "ToolbarFragment"
    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.INVISIBLE
        gridLayoutManager = if (layoutViewType == ViewTypes.GRID)
            GridLayoutManager(context, 2)
        else GridLayoutManager(context, 1)
        recyclerView.layoutManager = gridLayoutManager
    }

    /* I am going to use this method later to make an animation effect.
     For now if I do not use "changeViewType()" I will have animation, but loose listview */
    private fun updateAnimation() {
        if (layoutViewType == ViewTypes.GRID) gridLayoutManager?.spanCount = 2
        else gridLayoutManager?.spanCount = 1
        viewAdapter.notifyItemRangeChanged(0, viewAdapter.itemCount)
    }
}
