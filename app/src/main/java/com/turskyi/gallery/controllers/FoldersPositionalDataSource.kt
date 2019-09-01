package com.turskyi.gallery.controllers

import android.content.Context
import android.util.Log
import androidx.paging.PositionalDataSource
import com.turskyi.gallery.data.Constants
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.models.GalleryFolder

class FoldersPositionalDataSource(private val context: Context) : PositionalDataSource<GalleryFolder>() {

    private val repository = FilesRepository()

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<GalleryFolder>) {
        Log.d(Constants.TAG_DATA_SOURCE, "start = ${params.requestedStartPosition}, " +
                "load size =  ${params.requestedLoadSize}")
        val list = repository.getDataOfFolderList(params.requestedStartPosition, params.requestedLoadSize, context)
        callback.onResult(list, 0)
    }

    override fun loadRange(params: PositionalDataSource.LoadRangeParams, callback: PositionalDataSource.LoadRangeCallback<GalleryFolder>) {
        Log.d(Constants.TAG_DATA_SOURCE, "start = ${params.startPosition}," +
                " load size =  ${params.loadSize}")
        val list = repository.getDataOfFolderList(params.startPosition, params.loadSize, context)
        callback.onResult(list)
    }
}