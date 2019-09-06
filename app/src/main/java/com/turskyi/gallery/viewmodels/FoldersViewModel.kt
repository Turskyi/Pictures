package com.turskyi.gallery.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.GalleryPicture
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.models.ViewType.LINEAR

class FoldersViewModel(application: Application) : AndroidViewModel(application) {

    var selectedFolders: MutableList<GalleryFolder> = mutableListOf()
    var selectedImages: MutableList<GalleryPicture> = mutableListOf()
    var gridLayoutManager: GridLayoutManager? = null
//
//    private val repository = FilesRepository()

//    val listOfFolders = repository.getGalleryFolders(application)
//
    val viewTypes = MutableLiveData<ViewType>()
//
//
//    init {
//        LoadData(application).execute()
//    }

    fun updateLayoutView() {
        when {
            selectedFolders.isNotEmpty() -> viewTypes.value = ViewType.DELETE
            viewTypes.value == LINEAR -> viewTypes.value = ViewType.GRID
            else -> viewTypes.value = LINEAR
        }
    }

    fun setViewType(viewType: ViewType) {
        viewTypes.value = viewType
    }

//    @SuppressLint("StaticFieldLeak")
//    inner class LoadData(private val application: Application) : AsyncTask<Unit, Unit, MutableSet<GalleryFolder>>() {
//
//        override fun doInBackground(vararg p0: Unit?): MutableSet<GalleryFolder>? {
//            return repository.getGalleryFolders(application)
//        }
//
//        override fun onPostExecute(result: MutableSet<GalleryFolder>?) {
//            super.onPostExecute(result)
//            listOfFolders.value = result
//        }
//
//    }
}