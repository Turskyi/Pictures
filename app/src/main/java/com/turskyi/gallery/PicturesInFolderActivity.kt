package com.turskyi.gallery

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.turskyi.gallery.adapters.PicturesStaggeredViewAdapter
import com.turskyi.gallery.data.Constants.KEY_WORD_PATH
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.models.ViewTypes
import kotlinx.android.synthetic.main.fragment_pictures.*
import kotlinx.android.synthetic.main.toolbar.*

class PicturesInFolderActivity : AppCompatActivity() {

    private lateinit var viewAdapter: PicturesStaggeredViewAdapter
    private var layoutViewType: ViewTypes = ViewTypes.STAGGERED
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private val repository = FilesRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_pictures)

        btnArrowBack.setOnClickListener {
            onBackPressed()
        }

        val listOfPicturesInFolder = repository
            .getImagesInFolder(this)

        btnViewChanger.setOnClickListener {
            layoutViewType = if (layoutViewType.id == ViewTypes.LINEAR.id) {
                viewAdapter.changeViewType()
                btnViewChanger.setImageResource(R.drawable.ic_view_list_white)
                ViewTypes.STAGGERED
            } else {
                viewAdapter.changeViewType()
                btnViewChanger.setImageResource(R.drawable.ic_grid)
                ViewTypes.LINEAR
            }
            updateAnimation()
        }

        //I do not know where to use it yet
        val path = intent.getStringExtra(KEY_WORD_PATH)

        /** switch between listView and gridView */
        updateLayoutManager()

        viewAdapter = PicturesStaggeredViewAdapter(listOfPicturesInFolder)

        recyclerView.adapter = viewAdapter

        //my idea to update view after file has been deleted
//        FileLiveSingleton.getInstance().getUpdatedState().observe(this, Observer<Boolean> { isStateUpdated ->
//            if ( isStateUpdated  == true) {
//                context?.let { getImagesInFolder(it) }
//            }
//        })
    }

    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.VISIBLE
        staggeredGridLayoutManager =
            if (layoutViewType == ViewTypes.STAGGERED)
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            else StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
    }

    /* I will use the following method later to make an animation effect.
    For now, it works only if I do not use "changeViewType()"  */
    private fun updateAnimation() {
        if (layoutViewType == ViewTypes.STAGGERED)
            staggeredGridLayoutManager?.spanCount = 2
        else staggeredGridLayoutManager?.spanCount = 1
        viewAdapter.notifyItemRangeChanged(0, viewAdapter.itemCount)
    }
}