package com.turskyi.gallery.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.turskyi.gallery.controllers.domain.OnlineDataSourceFactory
import com.turskyi.gallery.controllers.domain.OnlinePicturesDataSource
import com.turskyi.gallery.models.OnlinePictureRepo
import com.turskyi.gallery.models.ViewType
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class OnlinePicturesViewModel(application: Application) : AndroidViewModel(application) {

    private var onlineDataSourceFactory: OnlineDataSourceFactory = OnlineDataSourceFactory()
    private var dataSourceMutableLiveData: MutableLiveData<OnlinePicturesDataSource>
    var executor: Executor
    var pagedListLiveData: LiveData<PagedList<OnlinePictureRepo>>

    init {
        dataSourceMutableLiveData = onlineDataSourceFactory.sourceLiveData
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10)
            .setPageSize(20)
            .setPrefetchDistance(4)
            .build()
        executor = Executors.newFixedThreadPool(5)
        pagedListLiveData =
            LivePagedListBuilder<Long, OnlinePictureRepo>(onlineDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build()
    }

    var selectedPictureRepos: MutableList<OnlinePictureRepo> = mutableListOf()
    var gridLayoutManager: GridLayoutManager? = null
    val viewTypes = MutableLiveData<ViewType?>()

    //TODO: Add "share" function
    fun updateLayoutView() {
        when {
            viewTypes.value == ViewType.LINEAR -> viewTypes.value = ViewType.GRID
            else -> viewTypes.value = ViewType.LINEAR
        }
    }

    fun setViewType(viewType: ViewType) {
        viewTypes.value = viewType
    }
}