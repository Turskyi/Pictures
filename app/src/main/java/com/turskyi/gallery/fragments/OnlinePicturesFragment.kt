package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import butterknife.ButterKnife
import com.turskyi.gallery.R.drawable.ic_view_list_white
import com.turskyi.gallery.adapters.OnlineGridAdapter
import com.turskyi.gallery.adapters.OnlineListAdapter
import com.turskyi.gallery.interfaces.OnOnlinePictureLongClickListener
import com.turskyi.gallery.models.OnlinePictureRepo
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewmodels.OnlinePicturesViewModel
import kotlinx.android.synthetic.main.fragment_online_pictures.*
import kotlinx.android.synthetic.main.toolbar.*

class OnlinePicturesFragment : Fragment(com.turskyi.gallery.R.layout.fragment_online_pictures),
    OnOnlinePictureLongClickListener {

    private lateinit var gridViewAdapter: OnlineGridAdapter
    private lateinit var listViewAdapter: OnlineListAdapter
    private lateinit var onlinePicturesViewModel: OnlinePicturesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ButterKnife.bind(activity!!)
        onlinePicturesViewModel =
            ViewModelProvider(activity!!).get(OnlinePicturesViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateFragment()
    }

    private fun updateFragment() {
        pagedListObserver()
        onlinePicturesViewModel.viewTypes.observe(this,
            Observer { viewType ->
                when (viewType) {
                    ViewType.GRID -> {
                        onlinePicturesViewModel.gridLayoutManager?.spanCount = 2
                        btnViewChanger.setImageResource(ic_view_list_white)
                    }
                    else -> {
                        onlinePicturesViewModel.gridLayoutManager?.spanCount = 1
                        btnViewChanger.setImageResource(com.turskyi.gallery.R.drawable.ic_grid)
                    }
                }
            })

        btnViewChanger.setOnClickListener {
            when {
                onlinePicturesViewModel.viewTypes.value == ViewType.GRID -> {
                    onlinePicturesViewModel.setViewType(ViewType.LINEAR)
                    onlineRecyclerView.adapter = listViewAdapter
                    //this method cannot be in "onlinePicturesViewModel.viewTypes.observe"
                    // because then it changes layout after delete
                    gridViewAdapter.changeViewType()
                    pagedListObserver()
                }
                onlinePicturesViewModel.viewTypes.value == ViewType.LINEAR -> {
                    onlinePicturesViewModel.setViewType(ViewType.GRID)
                    onlineRecyclerView.adapter = gridViewAdapter
                    gridViewAdapter.changeViewType()
                }
            }
        }

        gridViewAdapter = OnlineGridAdapter(this)
        listViewAdapter = OnlineListAdapter(this)

        gridViewAdapter.submitList(onlinePicturesViewModel.pagedListLiveData.value)

        /** get number of columns and switch between listView and gridView */
        updateLayoutManager()

        /* Without this line nothing going to show up */
        onlineRecyclerView.adapter = gridViewAdapter
    }

    private fun pagedListObserver() {
        onlinePicturesViewModel.pagedListLiveData.observe(this,
            Observer<PagedList<OnlinePictureRepo>> { pagedList ->
                if (onlinePicturesViewModel.viewTypes.value == ViewType.GRID) {
                    gridViewAdapter.submitList(pagedList)
                    onlineRecyclerView.adapter = gridViewAdapter
                } else {
                    listViewAdapter.submitList(pagedList)
                    onlineRecyclerView.adapter = listViewAdapter
                }
            })
    }

    override fun addOnLongClick(pictureRepo: OnlinePictureRepo) {
        onlinePicturesViewModel.selectedPictureRepos.add(pictureRepo)
        // I will use it when I will make "share function"
//        onlinePicturesViewModel.updateLayoutView()
    }

    override fun removeOnLongClick(pictureRepo: OnlinePictureRepo, viewType: ViewType) {
        // I will use it when I will make "share function"
//        onlinePicturesViewModel.updateLayoutView()
        onlinePicturesViewModel.selectedPictureRepos.remove(pictureRepo)
        if (onlinePicturesViewModel.selectedPictureRepos.isEmpty()) {
            onlinePicturesViewModel.viewTypes.value = viewType
        }
    }

    private fun updateLayoutManager() {
        btnArrowBack.visibility = View.INVISIBLE
        onlinePicturesViewModel.gridLayoutManager = GridLayoutManager(context, 2)
        onlinePicturesViewModel.viewTypes.value = ViewType.GRID
        onlineRecyclerView.layoutManager = onlinePicturesViewModel.gridLayoutManager
    }
}
