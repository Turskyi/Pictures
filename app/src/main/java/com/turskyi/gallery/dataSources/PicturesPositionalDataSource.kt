package com.turskyi.gallery.dataSources

import android.content.Context
import android.util.Log
import androidx.paging.PositionalDataSource
import com.turskyi.gallery.data.GalleryConstants
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.models.PictureUri

class PicturesPositionalDataSource(private val context: Context) :
    PositionalDataSource<PictureUri>() {

    private val repository = FilesRepository()

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<PictureUri>
    ) {
        val list = repository.getDataOfImageList(
            params.requestedStartPosition,
            params.requestedLoadSize,
            context
        )
        callback.onResult(list, 0)
    }

    override fun loadRange(
        params: LoadRangeParams,
        callback: LoadRangeCallback<PictureUri>
    ) {
        val list = repository.getDataOfImageList(
            params.startPosition,
            params.loadSize,
            context
        )
        callback.onResult(list)
    }
}