package com.turskyi.gallery.controllers

import android.content.Context
import android.util.Log
import androidx.paging.PositionalDataSource
import com.turskyi.gallery.data.Constants
import com.turskyi.gallery.data.FilesRepository
import com.turskyi.gallery.models.GalleryPicture
import java.security.cert.CertPath

class PicturesInFolderPositionalDataSource(private val context: Context, private val folderPath: String) : PositionalDataSource<GalleryPicture>() {

    private val repository = FilesRepository()

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<GalleryPicture>) {
        Log.d(Constants.TAG_DATA_SOURCE, "start = ${params.requestedStartPosition}, " +
                "load size =  ${params.requestedLoadSize}")
        val list = repository.getDataOfImagesInFolderList(params.requestedStartPosition, params.requestedLoadSize, context, folderPath)
        callback.onResult(list, 0)
    }

    override fun loadRange(params: PositionalDataSource.LoadRangeParams, callback: PositionalDataSource.LoadRangeCallback<GalleryPicture>) {
        Log.d(Constants.TAG_DATA_SOURCE, "start = ${params.startPosition}," +
                " load size =  ${params.loadSize}")
        val list = repository.getDataOfImagesInFolderList(params.startPosition, params.loadSize, context, folderPath)
        callback.onResult(list)
    }
}