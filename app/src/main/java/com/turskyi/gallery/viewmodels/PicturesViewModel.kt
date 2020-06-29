package com.turskyi.gallery.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.turskyi.gallery.dataSources.PicturesPositionalDataSource
import com.turskyi.gallery.models.PictureUri
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.utils.MainThreadExecutor
import java.util.concurrent.Executors

class PicturesViewModel(application: Application) : AndroidViewModel(application) {

    var selectedPictures: MutableList<PictureUri> = mutableListOf()
    val viewTypes = MutableLiveData<ViewType?>()
    var pagedList: PagedList<PictureUri>

    init {
        val dataSource = PicturesPositionalDataSource(application)

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
            selectedPictures.isNotEmpty() -> viewTypes.value = ViewType.DELETE
            viewTypes.value == ViewType.LINEAR -> viewTypes.value = ViewType.GRID
            else -> viewTypes.value = ViewType.LINEAR
        }
    }

    fun setViewType(viewType: ViewType){
        viewTypes.value = viewType
    }
}