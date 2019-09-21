package com.turskyi.gallery.models

/*
This has been copied from the Android paging library sample
 */
data class NetworkState(
    val status: Status,
    val msg: String? = null
) {
    companion object {
        val SUCCESS =
            NetworkState(Status.SUCCESS)
        val LOADING =
            NetworkState(Status.LOADING)
        fun error(msg: String?) =
            NetworkState(Status.FAILURE, msg)
    }
}
