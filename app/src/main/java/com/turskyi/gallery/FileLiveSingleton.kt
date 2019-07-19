package com.turskyi.gallery

import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.turskyi.gallery.model.MyFile

class FileLiveSingleton {

    companion object {

        private var INSTANCE: FileLiveSingleton? = null

        fun getInstance(): FileLiveSingleton {
            if (INSTANCE == null) INSTANCE = FileLiveSingleton()
            return INSTANCE!!
        }
    }

    private val livePath  = MutableLiveData<String>()

    fun getPath(): MutableLiveData<String> = livePath

    fun setPath(newPath: String) {
        livePath.value = newPath
    }

    fun setBackPath() {
        val pathArray = livePath.value!!.split("/")
        var exitPath = ""
        for (index in pathArray.indices) {
            if (index == ( pathArray.size - 2)) continue
            exitPath += "/${pathArray[index]}"
        }
        livePath.value = exitPath
    }
}