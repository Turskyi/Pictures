package com.turskyi.gallery.dataSources

import android.content.Context
import android.util.Log
import androidx.paging.PositionalDataSource
import com.turskyi.gallery.data.GalleryConstants
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.models.PictureUri

class PicturesInFolderPositionalDataSource(
    private val context: Context,
    private val folderPath: String
) :
    PositionalDataSource<PictureUri>() {

    private val repository = FilesRepository()

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<PictureUri>
    ) {
        Log.d(
            GalleryConstants.TAG_DATA_SOURCE, "start = ${params.requestedStartPosition}, " +
                    "load size =  ${params.requestedLoadSize}"
        )
        val list = repository.getDataOfImagesInFolderList(
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
        Log.d(
            GalleryConstants.TAG_DATA_SOURCE, "start = ${params.startPosition}," +
                    " load size =  ${params.loadSize}"
        )
        val list = repository.getDataOfImagesInFolderList(
            params.startPosition,
            params.loadSize,
            context
        )
        callback.onResult(list)
    }
}