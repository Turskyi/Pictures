package com.turskyi.gallery.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.models.ViewType.LINEAR

class FoldersViewModel(application: Application) : AndroidViewModel(application) {

    var selectedFolders: MutableList<GalleryFolder> = mutableListOf()
    var gridLayoutManager: GridLayoutManager? = null

    private val repository = FilesRepository()
    val listOfFolders = repository.getGalleryFolders(application)

    val viewTypes = MutableLiveData<ViewType>()
    var currentViewType: ViewType = ViewType.GRID

    fun changeLayoutView() {
        when {
            selectedFolders.isNotEmpty() -> viewTypes.value = ViewType.DELETE
            viewTypes.value == LINEAR -> viewTypes.value = ViewType.GRID
            else -> viewTypes.value = LINEAR
        }
    }
}