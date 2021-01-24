package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.turskyi.gallery.R.drawable.ic_view_list_white
import com.turskyi.gallery.adapters.OnlineGridAdapter
import com.turskyi.gallery.adapters.OnlineListAdapter
import com.turskyi.gallery.databinding.FragmentOnlinePicturesBinding
import com.turskyi.gallery.interfaces.OnOnlinePictureLongClickListener
import com.turskyi.gallery.models.OnlinePictureRepo
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewmodels.OnlinePicturesViewModel

class OnlinePicturesFragment : Fragment(com.turskyi.gallery.R.layout.fragment_online_pictures),
    OnOnlinePictureLongClickListener {

    private var _binding: FragmentOnlinePicturesBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var gridViewAdapter: OnlineGridAdapter
    private lateinit var listViewAdapter: OnlineListAdapter
    private lateinit var onlinePicturesViewModel: OnlinePicturesViewModel
    private var gridLayoutManager: GridLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnlinePicturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onlinePicturesViewModel =
            ViewModelProvider(requireActivity()).get(OnlinePicturesViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateFragment() {
        pagedListObserver()
        onlinePicturesViewModel.viewTypes.observe(viewLifecycleOwner,
            { viewType ->
                when (viewType) {
                    ViewType.GRID -> {
                        gridLayoutManager?.spanCount = 2
                        binding.toolbar.btnViewChanger.setImageResource(ic_view_list_white)
                    }
                    else -> {
                        gridLayoutManager?.spanCount = 1
                        binding.toolbar.btnViewChanger.setImageResource(com.turskyi.gallery.R.drawable.ic_grid)
                    }
                }
            })

        binding.toolbar.btnViewChanger.setOnClickListener {
            when (onlinePicturesViewModel.viewTypes.value) {
                ViewType.GRID -> {
                    onlinePicturesViewModel.setViewType(ViewType.LINEAR)
                    binding.onlineRecyclerView.adapter = listViewAdapter
                    //this method cannot be in "onlinePicturesViewModel.viewTypes.observe"
                    // because then it changes layout after delete
                    gridViewAdapter.changeViewType()
                    pagedListObserver()
                }
                ViewType.LINEAR -> {
                    onlinePicturesViewModel.setViewType(ViewType.GRID)
                    binding.onlineRecyclerView.adapter = gridViewAdapter
                    gridViewAdapter.changeViewType()
                }
                else -> {
                    onlinePicturesViewModel.setViewType(ViewType.LINEAR)
                    binding.onlineRecyclerView.adapter = listViewAdapter
                    //this method cannot be in "onlinePicturesViewModel.viewTypes.observe"
                    // because then it changes layout after delete
                    gridViewAdapter.changeViewType()
                    pagedListObserver()
                }
            }
        }

        gridViewAdapter = OnlineGridAdapter(this)
        listViewAdapter = OnlineListAdapter(this)

        gridViewAdapter.submitList(onlinePicturesViewModel.pagedListLiveData.value)
        listViewAdapter.submitList(onlinePicturesViewModel.pagedListLiveData.value)

        /** get number of columns and switch between listView and gridView */
        updateLayoutManager()
    }

    private fun pagedListObserver() {
        onlinePicturesViewModel.pagedListLiveData.observe(viewLifecycleOwner,
            { pagedList ->
                if (onlinePicturesViewModel.viewTypes.value == ViewType.GRID) {
                    gridViewAdapter.submitList(pagedList)
                    binding.onlineRecyclerView.adapter = gridViewAdapter
                } else {
                    listViewAdapter.submitList(pagedList)
                    binding.onlineRecyclerView.adapter = listViewAdapter
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
        binding.toolbar.btnArrowBack.visibility = View.INVISIBLE
        if (onlinePicturesViewModel.viewTypes.value == null || onlinePicturesViewModel.viewTypes.value == ViewType.GRID){
            onlinePicturesViewModel.viewTypes.value = ViewType.GRID
            gridLayoutManager = GridLayoutManager(context, 2)
            /* Without this line nothing going to show up */
            binding.onlineRecyclerView.adapter = gridViewAdapter
        } else if (onlinePicturesViewModel.viewTypes.value == ViewType.LINEAR){
            gridLayoutManager = GridLayoutManager(context, 1)
            binding.onlineRecyclerView.adapter = listViewAdapter
        }
        binding.onlineRecyclerView.layoutManager = gridLayoutManager
    }
}
