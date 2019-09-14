package com.turskyi.gallery.data

import androidx.recyclerview.widget.DiffUtil
import com.turskyi.gallery.models.Folder
import com.turskyi.gallery.models.OnlinePictureRepo
import com.turskyi.gallery.models.Picture

/**
 * API GalleryConstants for the Gallery app.
 */
object GalleryConstants {
    const val PERMISSION_EXTERNAL_STORAGE = 10001
    const val KEY_WORD_FRAGMENT_ID = "fragmentId"
    const val TAG_PICS_IN_FOLDER = "PicturesInFolderFragment(galleryFolder)"
    const val TAG_DETAILED_FRAGMENT = "DetailedFragment(galleryPicture)"
    const val TAG_DATA_SOURCE = "DataSource"
    const val GOOGLE_IMAGES = "https://www.google.com.ua/" +
            "search?hl=en&authuser=0&tbm=isch&source=hp&biw=1280&bih=689&ei=" +
            "F0xmXYCDEIqIrwTSr5DADg&q=images&oq=ima&gs_l=" +
            "img.1.0.0l10.5340.6441..7855...0.0..0.85.315.4......0....1..gws-wiz-img" +
            ".....0..35i39.AK7WdylJHzw"
    const val API_BASE_URL = "https://api.unsplash.com/"
    const val ACCESS_KEY = "0209b14130f2e2bd66173c802342c3d930704ba7415d44abe57a1d278e0002de"
}