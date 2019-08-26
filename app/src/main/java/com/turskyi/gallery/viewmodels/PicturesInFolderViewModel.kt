package com.turskyi.gallery.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.turskyi.gallery.models.GalleryPicture
import com.turskyi.gallery.models.ViewType

class PicturesInFolderViewModel(application: Application) : AndroidViewModel(application) {

    var selectedPictures: MutableList<GalleryPicture> = mutableListOf()
    var  staggeredGridLayoutManager: StaggeredGridLayoutManager? = null

//    val listOfPicturesInFolder = galleryFolder?.images

    val viewTypes = MutableLiveData<ViewType>()
    var currentViewType: ViewType = ViewType.STAGGERED

    fun changeLayoutView() {
        when {
            selectedPictures.isNotEmpty() -> viewTypes.value = ViewType.DELETE
            viewTypes.value == ViewType.LINEAR -> viewTypes.value = ViewType.STAGGERED
            else -> viewTypes.value = ViewType.LINEAR
        }
    }
}