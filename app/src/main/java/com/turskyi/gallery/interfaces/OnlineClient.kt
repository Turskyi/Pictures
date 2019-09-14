package com.turskyi.gallery.interfaces

import com.turskyi.gallery.models.OnlinePictureRepo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit endpoints definition.
 */
interface OnlineClient {
       @GET("photos/?client_id=0209b14130f2e2bd66173c802342c3d930704ba7415d44abe57a1d278e0002de")
    fun getOnlinePictures(
           @Query("id") id: String,
           @Query("page") page: Int,
           @Query("per_page") pageSize: Int
    ): Call<List<OnlinePictureRepo>>
}