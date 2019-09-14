package com.turskyi.gallery.controllers.domain

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.turskyi.gallery.models.OnlinePictureRepo

/**
 * Android paging library data source factory.
 * This will create the load photo data source.
 */
class OnlineDataSourceFactory :
    DataSource.Factory<Long, OnlinePictureRepo>() {

    private var source = OnlinePicturesDataSource()
    var sourceLiveData = MutableLiveData<OnlinePicturesDataSource>()

    init {
        sourceLiveData = MutableLiveData()
    }

    override fun create(): DataSource<Long, OnlinePictureRepo> {
        sourceLiveData.postValue(source)
        return source
    }
}