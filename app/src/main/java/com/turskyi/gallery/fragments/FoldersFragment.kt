package com.turskyi.gallery.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.turskyi.gallery.R
import com.turskyi.gallery.R.drawable.ic_view_list_white
import com.turskyi.gallery.adapters.FolderGridAdapter
import com.turskyi.gallery.adapters.FolderListAdapter
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.data.GalleryConstants
import com.turskyi.gallery.databinding.FragmentFoldersBinding
import com.turskyi.gallery.databinding.FragmentPicturesBinding
import com.turskyi.gallery.interfaces.OnFolderLongClickListener
import com.turskyi.gallery.models.Folder
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewmodels.FoldersViewModel
import java.io.File

class FoldersFragment : Fragment(), OnFolderLongClickListener {

    private var _binding: FragmentFoldersBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var foldersViewModel: FoldersViewModel
    private lateinit var gridViewAdapter: FolderGridAdapter
    private lateinit var listViewAdapter: FolderListAdapter
    private var gridLayoutManager: GridLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoldersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foldersViewModel = ViewModelProvider(requireActivity()).get(FoldersViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // I made a function out of this code here because I have to call it again,
        // when I delete the folder
        updateFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateFragment() {

        foldersViewModel.viewTypes.observe(viewLifecycleOwner, { viewType ->
            when (viewType) {
                ViewType.DELETE -> {
                    binding.toolbar.btnViewChanger.setImageResource(R.drawable.ic_remove32)
                }
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
                foldersViewModel.selectedFolders.size > 0 -> deleteAllSelected()
                foldersViewModel.viewTypes.value == ViewType.GRID -> {
                    foldersViewModel.setViewType(ViewType.LINEAR)
                    listViewAdapter.submitList(foldersViewModel.pagedList)
                    binding.foldersRecyclerView.adapter = listViewAdapter
                    gridViewAdapter.changeViewType()
                }
                foldersViewModel.viewTypes.value == ViewType.LINEAR -> {
                    foldersViewModel.setViewType(ViewType.GRID)
                    gridViewAdapter.submitList(foldersViewModel.pagedList)
                    binding.foldersRecyclerView.adapter = gridViewAdapter
                    gridViewAdapter.changeViewType()
                }
            }
        }

        gridViewAdapter = FolderGridAdapter(this)
        listViewAdapter = FolderListAdapter(this)

        gridViewAdapter.submitList(foldersViewModel.pagedList)
        listViewAdapter.submitList(foldersViewModel.pagedList)

        checkIfListEmpty(foldersViewModel.pagedList)

        /** get number of columns and switch between listView and gridView */
        updateLayoutManager()
    }

    private fun checkIfListEmpty(pagedList: PagedList<Folder>) {
        val fragmentActivity: FragmentActivity? = activity
        if (pagedList.size == 0) {
            setUpAnEmptyViewImage(fragmentActivity)
        } else {
            (fragmentActivity?.supportFragmentManager
                ?.findFragmentById(R.id.bottomNavigationView) as BottomNavigationFragment?)
                ?.setVisibility(View.VISIBLE)
        }
    }

    override fun addOnLongClick(folder: Folder) {
        val repository = FilesRepository()
        foldersViewModel.selectedFolders.add(folder)
/* adds all images, located in the selected folder to the selected pictures list */
        for (i in activity?.applicationContext?.let {
            repository.getSetOfImagesInFolder(it)
        }!!) {
            foldersViewModel.selectedImages.add(i)
        }
        foldersViewModel.updateLayoutView()
    }

    override fun removeOnLongClick(folder: Folder, viewType: ViewType) {
        foldersViewModel.updateLayoutView()
        foldersViewModel.selectedFolders.remove(folder)
        if (foldersViewModel.selectedFolders.isEmpty()) {
            foldersViewModel.viewTypes.value = viewType
        }
    }

    private fun setUpAnEmptyViewImage(fragmentActivity: FragmentActivity?) {
        binding.emptyFolders.visibility = View.VISIBLE
        (fragmentActivity?.supportFragmentManager
            ?.findFragmentById(R.id.bottomNavigationView) as BottomNavigationFragment?)
            ?.setVisibility(GONE)
        binding.toolbar.btnViewChanger.visibility = GONE
        binding.emptyFolders.setOnClickListener(sendToGoogleImages)
    }

    private var sendToGoogleImages: View.OnClickListener = View.OnClickListener {
        val url = GalleryConstants.GOOGLE_IMAGES
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun deleteAllSelected() {
        foldersViewModel.selectedFolders.let {
            for (selectedFolder in it) {
                val folder = File(selectedFolder.folderPath)
                if (folder.exists() && folder.isDirectory) {
                    // delete all pictures in folders
                    foldersViewModel.selectedImages.let {
//TODO: delete images
                    }
                    //TODO: Delete empty folder
                    folder.delete()
                    updateFragment()
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.folder_does_not_exist),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun updateLayoutManager() {
        binding.toolbar.btnArrowBack.visibility = View.INVISIBLE
        if (foldersViewModel.viewTypes.value == null ||
            foldersViewModel.viewTypes.value == ViewType.GRID
        ) {
            foldersViewModel.viewTypes.value = ViewType.GRID
            gridLayoutManager = GridLayoutManager(context, 2)
            /* Without this line nothing gonna shows up */
            binding.foldersRecyclerView.adapter = gridViewAdapter
        } else if (foldersViewModel.viewTypes.value == ViewType.LINEAR) {
            gridLayoutManager = GridLayoutManager(context, 1)
            binding.foldersRecyclerView.adapter = listViewAdapter
        }
        binding.foldersRecyclerView.layoutManager = gridLayoutManager
    }
}
