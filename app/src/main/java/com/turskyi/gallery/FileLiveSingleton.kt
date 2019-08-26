package com.turskyi.gallery

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.turskyi.gallery.data.Constants

//TODO: Do I need this pattern for future use?
class FileLiveSingleton {
    //TODO можна наслідувати мутабл лайв дата і робити ці операції зразу в ньому
    // почитай про кастомні livedata,
    // а якщо є операції що не зрозуміло як роботи в ній, то винось в viewModel
    companion object {
        private var INSTANCE: FileLiveSingleton? = null
        fun getInstance(): FileLiveSingleton {
            INSTANCE?: run {
                INSTANCE = FileLiveSingleton()
            }
            return INSTANCE!!
        }
    }

    private val liveUpdatedListState  = MutableLiveData<Boolean>()

    fun getUpdatedState(): MutableLiveData<Boolean> = liveUpdatedListState

    private val livePath  = MutableLiveData<String>()
    fun getPath(): MutableLiveData<String> = livePath

    fun setPath(newPath: String) {
        Log.d("FileSingleton","firstPicturePath $newPath")
        livePath.value = newPath
    }

    fun setBackPath() {
        livePath.value?.let{

            val pathArray = livePath.value!!.split("/")
            var exitPath = ""
            for (index in pathArray.indices) {
                if (index == ( pathArray.size - 2) || index == 0) continue
                exitPath += "/${pathArray[index]}"
            }
            if (exitPath == Constants.EMULATED_FOLDER){
                exitPath = Constants.STORAGE_FOLDER
            }
            livePath.value = exitPath
        }
    }
}