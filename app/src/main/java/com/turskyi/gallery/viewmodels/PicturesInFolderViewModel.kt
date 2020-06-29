package com.turskyi.gallery.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.turskyi.gallery.dataSources.PicturesInFolderPositionalDataSource
import com.turskyi.gallery.models.Folder
import com.turskyi.gallery.models.PictureUri
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.utils.MainThreadExecutor
import java.util.concurrent.Executors

class PicturesInFolderViewModel(application: Application, folder: Folder?) :
    AndroidViewModel(application) {

    var selectedPictureUris: MutableList<PictureUri> = mutableListOf()
    val viewTypes = MutableLiveData<ViewType>()

    var pagedList: PagedList<PictureUri>

    init {
        val dataSource = PicturesInFolderPositionalDataSource(
            application, folder!!.folderPath
        )

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()

        pagedList = PagedList.Builder(dataSource, config)
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .setNotifyExecutor(MainThreadExecutor())
            .build()
    }

    fun changeLayoutView() {
        when {
            selectedPictureUris.isNotEmpty() -> viewTypes.value = ViewType.DELETE
            viewTypes.value == ViewType.LINEAR -> viewTypes.value = ViewType.STAGGERED
            else -> viewTypes.value = ViewType.LINEAR
        }
    }

    fun setViewType(viewType: ViewType) {
        viewTypes.value = viewType
    }
}