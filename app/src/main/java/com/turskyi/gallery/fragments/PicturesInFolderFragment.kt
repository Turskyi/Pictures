package com.turskyi.gallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.turskyi.gallery.R
import com.turskyi.gallery.adapters.PictureInFolderListAdapter
import com.turskyi.gallery.adapters.PictureInFolderStaggeredAdapter
import com.turskyi.gallery.databinding.FragmentPicturesBinding
import com.turskyi.gallery.interfaces.IOnBackPressed
import com.turskyi.gallery.interfaces.OnPictureLongClickListener
import com.turskyi.gallery.models.Folder
import com.turskyi.gallery.models.PictureUri
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewmodels.ModelFactory
import com.turskyi.gallery.viewmodels.PicturesInFolderViewModel

class PicturesInFolderFragment(private val folder: Folder?) : Fragment(),
    OnPictureLongClickListener,
    IOnBackPressed {

    private var _binding: FragmentPicturesBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var picturesInFolderViewModel: PicturesInFolderViewModel
    private lateinit var staggeredViewAdapter: PictureInFolderStaggeredAdapter
    private lateinit var listViewAdapter: PictureInFolderListAdapter
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPicturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        picturesInFolderViewModel = ViewModelProvider(
            requireActivity(), ModelFactory(
                requireActivity().application, folder
            )
        )
            .get(PicturesInFolderViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateFragment()
    }

    override fun onBackPressed() {
        activity?.supportFragmentManager?.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateFragment() {
        binding.toolbar.btnArrowBack.setOnClickListener {
            onBackPressed()
        }

        picturesInFolderViewModel.viewTypes.observe(viewLifecycleOwner, { viewType ->
            when (viewType) {
                ViewType.DELETE -> {
                    binding.toolbar.btnViewChanger.setImageResource(R.drawable.ic_remove32)
                }
                ViewType.STAGGERED -> {
                    staggeredGridLayoutManager?.spanCount = 2
                    binding.toolbar.btnViewChanger.setImageResource(R.drawable.ic_view_list_white)
                }
                else -> {
                    staggeredGridLayoutManager?.spanCount = 1
                    binding.toolbar.btnViewChanger.setImageResource(R.drawable.ic_grid)
                }
            }
        })

        binding.toolbar.btnViewChanger.setOnClickListener {
            when {
                picturesInFolderViewModel.selectedPictureUris.size > 0 -> deleteAllSelected()
                picturesInFolderViewModel.viewTypes.value == ViewType.STAGGERED -> {
                    picturesInFolderViewModel.setViewType(ViewType.LINEAR)
                    listViewAdapter.submitList(picturesInFolderViewModel.pagedList)
                    binding.picturesRecyclerView.adapter = listViewAdapter
                    staggeredViewAdapter.changeViewType()
                }
                picturesInFolderViewModel.viewTypes.value == ViewType.LINEAR -> {
                    picturesInFolderViewModel.setViewType(ViewType.STAGGERED)
                    binding.picturesRecyclerView.adapter = staggeredViewAdapter
                    staggeredViewAdapter.changeViewType()
                }
            }
        }

        staggeredViewAdapter = context?.let {
            PictureInFolderStaggeredAdapter(this)
        }!!
        listViewAdapter = context?.let {
            PictureInFolderListAdapter(this)
        }!!

        staggeredViewAdapter.submitList(picturesInFolderViewModel.pagedList)
        listViewAdapter.submitList(picturesInFolderViewModel.pagedList)

        checkIfListEmpty(picturesInFolderViewModel.pagedList)

        updateLayoutManager()
    }

    private fun checkIfListEmpty(pagedList: PagedList<PictureUri>) {
        val fragmentActivity: FragmentActivity? = activity
        if (pagedList.size < 1) {
            onBackPressed()
        } else {
            (fragmentActivity?.supportFragmentManager
                ?.findFragmentById(R.id.bottomNavigationView) as BottomNavigationFragment?)
                ?.setVisibility(VISIBLE)
        }
    }

    override fun addOnLongClick(pictureUri: PictureUri) {
        picturesInFolderViewModel.selectedPictureUris.add(pictureUri)
        picturesInFolderViewModel.changeLayoutView()
    }

    override fun removeOnLongClick(pictureUri: PictureUri, viewType: ViewType) {
        picturesInFolderViewModel.changeLayoutView()
        picturesInFolderViewModel.selectedPictureUris.remove(pictureUri)
        if (picturesInFolderViewModel.selectedPictureUris.isEmpty()) {
            picturesInFolderViewModel.viewTypes.value = viewType
        }
    }

    private fun deleteAllSelected() {
        picturesInFolderViewModel.selectedPictureUris.let {
            for (selectedPicture in it) {
//TODO implement deleting
            }
        }
    }

    private fun updateLayoutManager() {
        binding.toolbar.btnArrowBack.visibility = VISIBLE
        if (picturesInFolderViewModel.viewTypes.value == null ||
            picturesInFolderViewModel.viewTypes.value == ViewType.STAGGERED
        ) {
            picturesInFolderViewModel.viewTypes.value = ViewType.STAGGERED
            staggeredGridLayoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            binding.picturesRecyclerView.adapter = staggeredViewAdapter
        } else if (picturesInFolderViewModel.viewTypes.value == ViewType.LINEAR) {
            staggeredGridLayoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            binding.picturesRecyclerView.adapter = listViewAdapter
        }
        binding.picturesRecyclerView.layoutManager = staggeredGridLayoutManager
    }
}