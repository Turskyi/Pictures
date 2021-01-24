package com.turskyi.gallery.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.turskyi.gallery.R
import com.turskyi.gallery.R.drawable.ic_view_list_white
import com.turskyi.gallery.adapters.PictureGridAdapter
import com.turskyi.gallery.adapters.PictureListAdapter
import com.turskyi.gallery.data.GalleryConstants
import com.turskyi.gallery.databinding.FragmentPicturesBinding
import com.turskyi.gallery.interfaces.OnPictureLongClickListener
import com.turskyi.gallery.models.PictureUri
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewmodels.PicturesViewModel

class PicturesFragment : Fragment(), OnPictureLongClickListener {

    private var _binding: FragmentPicturesBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var gridViewAdapter: PictureGridAdapter
    private lateinit var listViewAdapter: PictureListAdapter
    private lateinit var picturesViewModel: PicturesViewModel
    private var gridLayoutManager: GridLayoutManager? = null

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
        picturesViewModel = ViewModelProvider(requireActivity()).get(PicturesViewModel::class.java)
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

        picturesViewModel.viewTypes.observe(viewLifecycleOwner, { viewType ->
            when (viewType) {
                ViewType.DELETE -> binding.toolbar.btnViewChanger.setImageResource(R.drawable.ic_remove32)
                ViewType.GRID -> {
                    gridLayoutManager?.spanCount = 2
                    binding.toolbar.btnViewChanger.setImageResource(ic_view_list_white)
                }
                else -> {
                    gridLayoutManager?.spanCount = 1
                    binding.toolbar.btnViewChanger.setImageResource(R.drawable.ic_grid)
                }
            }
        })

        binding.toolbar.btnViewChanger.setOnClickListener {
            when {
                picturesViewModel.selectedPictures.size > 0 -> deleteAllSelected()
                picturesViewModel.viewTypes.value == ViewType.GRID -> {
                    picturesViewModel.setViewType(ViewType.LINEAR)
                    listViewAdapter.submitList(picturesViewModel.pagedList)
                    binding.picturesRecyclerView.adapter = listViewAdapter
                    /*this method cannot be in "picturesViewModel.viewTypes.observe"
                     because then it changes layout after delete */
                    gridViewAdapter.changeViewType()
                }
                picturesViewModel.viewTypes.value == ViewType.LINEAR -> {
                    picturesViewModel.setViewType(ViewType.GRID)
                    gridViewAdapter.submitList(picturesViewModel.pagedList)
                    binding.picturesRecyclerView.adapter = gridViewAdapter
                    gridViewAdapter.changeViewType()
                }
            }
        }

        gridViewAdapter = PictureGridAdapter(this)
        listViewAdapter = PictureListAdapter(this)

        gridViewAdapter.submitList(picturesViewModel.pagedList)
        listViewAdapter.submitList(picturesViewModel.pagedList)

        checkIfListEmpty(picturesViewModel.pagedList)

        /**
         *@Description gets number of columns and switch between listView and gridView
         * */
        updateLayoutManager()

    }

    private fun checkIfListEmpty(pagedList: PagedList<PictureUri>) {
        val fragmentActivity: FragmentActivity? = activity
        if (pagedList.size < 1) {
            setUpAnEmptyViewImage(fragmentActivity)
        } else {
            (fragmentActivity?.supportFragmentManager
                ?.findFragmentById(R.id.bottomNavigationView) as BottomNavigationFragment?)
                ?.setVisibility(VISIBLE)
        }
    }

    override fun addOnLongClick(pictureUri: PictureUri) {
        picturesViewModel.selectedPictures.add(pictureUri)
        picturesViewModel.updateLayoutView()
    }

    override fun removeOnLongClick(pictureUri: PictureUri, viewType: ViewType) {
        picturesViewModel.updateLayoutView()
        picturesViewModel.selectedPictures.remove(pictureUri)
        if (picturesViewModel.selectedPictures.isEmpty()) {
            picturesViewModel.viewTypes.value = viewType
        }
    }

    private fun setUpAnEmptyViewImage(fragmentActivity: FragmentActivity?) {
        binding.emptyView.visibility = VISIBLE
        (fragmentActivity?.supportFragmentManager
            ?.findFragmentById(R.id.bottomNavigationView) as BottomNavigationFragment?)
            ?.setVisibility(GONE)
        binding.toolbar.btnViewChanger.visibility = GONE
        binding.emptyView.setOnClickListener(sendToGoogleImages)
    }

    private var sendToGoogleImages: View.OnClickListener = View.OnClickListener {
        val url = GalleryConstants.GOOGLE_IMAGES
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun deleteAllSelected() {
        picturesViewModel.selectedPictures.let {
            for (selectedPicture in it) {
//                TODO: implement deleting
            }
        }
    }

    private fun updateLayoutManager() {
        binding.toolbar.btnArrowBack.visibility = View.INVISIBLE
        if (picturesViewModel.viewTypes.value == null
            || picturesViewModel.viewTypes.value == ViewType.GRID
        ) {
            picturesViewModel.viewTypes.value = ViewType.GRID
            gridLayoutManager = GridLayoutManager(context, 2)
            /* Without this line nothing going to show up */
            binding.picturesRecyclerView.adapter = gridViewAdapter
            binding.picturesRecyclerView.layoutManager = gridLayoutManager
        } else if (picturesViewModel.viewTypes.value == ViewType.LINEAR) {
            gridLayoutManager = GridLayoutManager(context, 1)
            /* Without this line nothing going to show up */
            binding.picturesRecyclerView.adapter = listViewAdapter
            binding.picturesRecyclerView.layoutManager = gridLayoutManager
        }
        binding.picturesRecyclerView.layoutManager = gridLayoutManager
    }
}
