package com.turskyi.gallery.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.models.GalleryPicture

class PicturesViewModel(application: Application) : AndroidViewModel(application) {

    var selectedPictures: MutableList<GalleryPicture> = mutableListOf()
    var gridLayoutManager: GridLayoutManager? = null

    private val repository = FilesRepository()
    val listOfPictures = repository.getGalleryImages(application)

    val viewTypes = MutableLiveData<ViewType?>()

    fun updateLayoutView() {
        when {
            selectedPictures.isNotEmpty() -> viewTypes.value = ViewType.DELETE
            viewTypes.value == ViewType.LINEAR -> viewTypes.value = ViewType.GRID
            else -> viewTypes.value = ViewType.LINEAR
        }
    }

    fun setViewType(viewType: ViewType){
        viewTypes.value = viewType
    }
}