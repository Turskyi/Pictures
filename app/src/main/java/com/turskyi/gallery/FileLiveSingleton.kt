package com.turskyi.gallery

import android.util.Log
import androidx.lifecycle.MutableLiveData

class FileLiveSingleton {

    companion object {

        private var INSTANCE: FileLiveSingleton? = null

        fun getInstance(): FileLiveSingleton {
            INSTANCE?: run {
                INSTANCE = FileLiveSingleton()
            }
            return INSTANCE!!
        }
    }

    private val livePath  = MutableLiveData<String>()

    fun getPath(): MutableLiveData<String> = livePath

    fun setPath(newPath: String) {
        Log.d("FileSingleton","path $newPath")
        livePath.value = newPath
    }

    fun setBackPath() {

        val pathArray = livePath.value!!.split("/")

        var exitPath = ""

        for (index in pathArray.indices) {
            if (index == ( pathArray.size - 2) || index == 0) continue
            exitPath += "/${pathArray[index]}"
        }

        if (exitPath == "/storage/emulated/"){
            exitPath = "/storage/"
        }
        livePath.value = exitPath
    }
}