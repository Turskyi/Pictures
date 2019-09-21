package com.turskyi.gallery.dataSources

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.turskyi.gallery.data.GalleryConstants
import com.turskyi.gallery.data.RetrofitClientInstance
import com.turskyi.gallery.interfaces.OnlineClient
import com.turskyi.gallery.models.NetworkState
import com.turskyi.gallery.models.OnlinePictureRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Android paging library data source.
 * This will load the pictures and allow an infinite scroll on the screen.
 */
class OnlinePicturesDataSource :
    PageKeyedDataSource<Long, OnlinePictureRepo>() {

    val networkState = MutableLiveData<NetworkState>()
    private var lastPage: Int? = null

    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, OnlinePictureRepo>
    ) {
        Log.d(
            "onlineData", "start = ${params.placeholdersEnabled}, " +
                    "load size =  ${params.requestedLoadSize}"
        )
        // updating the network state to loading
        networkState.postValue(NetworkState.LOADING)
        // api call for the first page
        val onlineClient: OnlineClient = RetrofitClientInstance.getOnlineClient()
        val call =
            onlineClient.getOnlinePictures(GalleryConstants.ACCESS_KEY, 0, params.requestedLoadSize)
        call.enqueue(object : Callback<List<OnlinePictureRepo>> {

            override fun onResponse(
                call: Call<List<OnlinePictureRepo>>,
                response: Response<List<OnlinePictureRepo>>
            ) {
                // if the response is successful
                // we get the last page number
                // we push the result on the paging callback
                // we update the network state to success
                lastPage = response.headers().get("x-total")?.toInt()?.div(params.requestedLoadSize)
                val pictureList = response.body()!!
                callback.onResult(pictureList, null, 2.toLong())
                networkState.postValue(NetworkState.SUCCESS)
            }

            override fun onFailure(call: Call<List<OnlinePictureRepo>>, t: Throwable) {
                // we update the network state to error along with the error message
                networkState.postValue(NetworkState.error(t.message))
            }
        })
    }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Long>,
        callback: PageKeyedDataSource.LoadCallback<Long, OnlinePictureRepo>
    ) {
        // updating the network state to loading
        networkState.postValue(NetworkState.LOADING)
        // api call for the subsequent pages
        val onlineClient = RetrofitClientInstance.getOnlineClient()
        val call = onlineClient.getOnlinePictures(
            GalleryConstants.ACCESS_KEY,
            params.key.toInt() + 1,
            params.requestedLoadSize
        )
        call.enqueue(object : Callback<List<OnlinePictureRepo>> {
            override fun onResponse(
                call: Call<List<OnlinePictureRepo>>,
                response: Response<List<OnlinePictureRepo>>
            ) {
                // if the response is successful
                // we get the last page number
                // we push the result on the paging callback
                // we update the network state to success
                val nextPage =
                    if (params.key == lastPage?.toLong()) {
                        null
                    } else {
                        params.key + 1
                    }
                callback.onResult(response.body()!!, nextPage)
                networkState.postValue(NetworkState.SUCCESS)
            }

            // if the response is not successful
            // we update the network state to error along with the error message
            override fun onFailure(call: Call<List<OnlinePictureRepo>>, t: Throwable) {
                // we update the network state to error along with the error message
                networkState.postValue(NetworkState.error(t.message))
            }
        })
    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Long>,
        callback: PageKeyedDataSource.LoadCallback<Long, OnlinePictureRepo>
    ) {
        // we do nothing here because everything will be loaded
    }
}