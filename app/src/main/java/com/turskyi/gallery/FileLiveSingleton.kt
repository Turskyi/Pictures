package com.turskyi.gallery

import androidx.lifecycle.MutableLiveData

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
        if (exitPath.endsWith( "/storage/emulated/")){
            exitPath = "/storage/"
        }
        livePath.value = exitPath
    }
}