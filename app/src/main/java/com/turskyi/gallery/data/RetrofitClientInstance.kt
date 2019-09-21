package com.turskyi.gallery.data

import com.turskyi.gallery.interfaces.OnlineClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** This is singleton which initializing only one time */
class RetrofitClientInstance {

    companion object {

        private var onlineClient: OnlineClient? = null

        fun getOnlineClient(): OnlineClient{
                onlineClient?: run {
               val retrofit: Retrofit = retrofit2.Retrofit.Builder()
                   .baseUrl(GalleryConstants.API_BASE_URL)
                   .addConverterFactory(GsonConverterFactory.create())
                   .build()
                onlineClient = retrofit.create(OnlineClient::class.java)
            }
            return onlineClient!!
        }
    }
}