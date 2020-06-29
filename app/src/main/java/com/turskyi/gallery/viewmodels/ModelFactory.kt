package com.turskyi.gallery.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.turskyi.gallery.models.Folder

class ModelFactory(private val application: Application, private val folder: Folder?) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

//        return if (modelClass == PicturesInFolderViewModel::class.java){
//            PicturesInFolderViewModel(application, folder) as T
//        } else null

        //I replaced this code with code above to avoid error
        @Suppress("UNCHECKED_CAST")
        return PicturesInFolderViewModel(application, folder) as T
    }
}