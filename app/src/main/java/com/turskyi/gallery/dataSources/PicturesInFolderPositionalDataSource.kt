package com.turskyi.gallery.dataSources

import android.content.Context
import android.util.Log
import androidx.paging.PositionalDataSource
import com.turskyi.gallery.data.GalleryConstants
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.models.Picture

class PicturesInFolderPositionalDataSource(
    private val context: Context,
    private val folderPath: String
) :
    PositionalDataSource<Picture>() {

    private val repository = FilesRepository()

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<Picture>
    ) {
        Log.d(
            GalleryConstants.TAG_DATA_SOURCE, "start = ${params.requestedStartPosition}, " +
                    "load size =  ${params.requestedLoadSize}"
        )
        val list = repository.getDataOfImagesInFolderList(
            params.requestedStartPosition,
            params.requestedLoadSize,
            context,
            folderPath
        )
        callback.onResult(list, 0)
    }

    override fun loadRange(
        params: PositionalDataSource.LoadRangeParams,
        callback: PositionalDataSource.LoadRangeCallback<Picture>
    ) {
        Log.d(
            GalleryConstants.TAG_DATA_SOURCE, "start = ${params.startPosition}," +
                    " load size =  ${params.loadSize}"
        )
        val list = repository.getDataOfImagesInFolderList(
            params.startPosition,
            params.loadSize,
            context,
            folderPath
        )
        callback.onResult(list)
    }
}