package com.turskyi.gallery.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.turskyi.gallery.models.Picture
import com.turskyi.gallery.models.ViewType

class PicturesInFolderViewModel(application: Application) : AndroidViewModel(application) {

    var selectedPictures: MutableList<Picture> = mutableListOf()
    val viewTypes = MutableLiveData<ViewType>()

//    private val repository = FilesRepository()
//    private val listOfFolders = repository.getSetOfFolders(application)
//    var pagedList: PagedList<Picture>
//
//    init {
//     val dataSource = PicturesInFolderPositionalDataSource(
//            application, /* ??? */
//        )
//
//        val config: PagedList.Config = PagedList.Config.Builder()
//            .setEnablePlaceholders(false)
//            .setPageSize(10)
//            .build()
//
//        pagedList = PagedList.Builder(dataSource, config)
//            .setFetchExecutor(Executors.newSingleThreadExecutor())
//            .setNotifyExecutor(MainThreadExecutor())
//            .build()
//    }

    fun changeLayoutView() {
        when {
            selectedPictures.isNotEmpty() -> viewTypes.value = ViewType.DELETE
            viewTypes.value == ViewType.LINEAR -> viewTypes.value = ViewType.STAGGERED
            else -> viewTypes.value = ViewType.LINEAR
        }
    }

    fun setViewType(viewType: ViewType) {
        viewTypes.value = viewType
    }
}