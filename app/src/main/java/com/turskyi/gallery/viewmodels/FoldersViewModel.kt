package com.turskyi.gallery.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.turskyi.gallery.dataSources.FoldersPositionalDataSource
import com.turskyi.gallery.utils.MainThreadExecutor
import com.turskyi.gallery.models.Folder
import com.turskyi.gallery.models.PictureUri
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.models.ViewType.LINEAR
import java.util.concurrent.Executors

class FoldersViewModel(application: Application) : AndroidViewModel(application) {

    var selectedFolders: MutableList<Folder> = mutableListOf()
    var selectedImages: MutableList<PictureUri> = mutableListOf()
    val viewTypes = MutableLiveData<ViewType>()
    var pagedList: PagedList<Folder>

    init {
        val dataSource =
            FoldersPositionalDataSource(application)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()

        pagedList = PagedList.Builder(dataSource, config)
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .setNotifyExecutor(MainThreadExecutor())
            .build()
    }

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

    /* I would use the following method if I did not use the pagedList */
//        private val repository = FilesRepository()
//    val listOfFolders = repository.getGalleryFolders(application)

//    init {
//        LoadData(application).execute()
//    }

//    @SuppressLint("StaticFieldLeak")
//    inner class LoadData(private val application: Application) : AsyncTask<Unit, Unit, MutableSet<Folder>>() {
//
//        override fun doInBackground(vararg p0: Unit?): MutableSet<Folder>? {
//            return repository.getGalleryFolders(application)
//        }
//
//        override fun onPostExecute(result: MutableSet<Folder>?) {
//            super.onPostExecute(result)
//            listOfFolders.value = result
//        }
//    }
}