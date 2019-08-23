package com.turskyi.gallery.data

import android.content.Context
import android.provider.MediaStore
import android.provider.MediaStore.Images
import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.GalleryPicture

class FilesRepository {

    /** finds pictures, nothing else */
    fun getGalleryImages(context: Context): MutableList<GalleryPicture> {

        /** Get all columns of type images */
        val columns = arrayOf(
            Images.Media.DATA,
            Images.Media._ID
        )

        /** Create a list of urls pictures */
        val galleryImageUrls: MutableList<GalleryPicture> = mutableListOf()

        /** order data by date */
        val orderBy = Images.Media.DATE_TAKEN

        /** This cursor will hold the result of the query
        and put all data in Cursor by sorting in descending order */
        val cursor = context.contentResolver
            .query(
                Images.Media.EXTERNAL_CONTENT_URI,
                columns, null, null, "$orderBy DESC"
            )

        cursor?.let {
            if (it.moveToNext())
                do {
                    val dataColumnIndex = it.getColumnIndex(Images.Media.DATA)
                    val id =
                        it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val galleryPicture = GalleryPicture(it.getString(dataColumnIndex), id)
                    galleryImageUrls.add(galleryPicture)
                } while (it.moveToNext())
            it.close()
        }
        return galleryImageUrls
    }

    fun getGalleryFolders(context: Context): MutableSet<GalleryFolder> {

        val columns = arrayOf(
            Images.Media.DATA,
            Images.Media._ID
        )

        /** My set of folders */
        val galleryFolderUrls: MutableSet<GalleryFolder> = mutableSetOf()
        val galleryFolderPaths: MutableSet<String> = mutableSetOf()

        /** order data by date */
        val orderBy = Images.Media.DATE_TAKEN

        /** This cursor will hold the result of the query
         * and get all data in Cursor by sorting in descending order */
        val cursor = context.contentResolver.query(
            Images.Media.EXTERNAL_CONTENT_URI,
            columns,
            null,
            null,
            "$orderBy DESC"
        )

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val dataColumnIndex = cursor.getColumnIndex(Images.Media.DATA)
                val picturePath = cursor.getString(dataColumnIndex)
                val pathArrayOfStrings = picturePath.split('/')
                val folderName = pathArrayOfStrings[pathArrayOfStrings.size - 2]
                val folderPath = getFolderPath(pathArrayOfStrings)

                // if we found copy of the folder path we skip the rest of the function
                // and go to the next picture
                if (galleryFolderPaths.contains(folderPath)) continue

                galleryFolderPaths.add(folderPath)

                val images = getImagesInFolder(context, folderPath)
                galleryFolderUrls.add(
                    GalleryFolder(folderPath, picturePath, folderName, images)
                )
            } while (cursor.moveToNext())
            cursor.close()
        }
        return galleryFolderUrls
    }

    private fun getFolderPath(pathArrayOfStrings: List<String>): String {
        var folderPath = ""

        /** building a firstPicturePath string to the folder */
        for (index in pathArrayOfStrings.indices) {
            if (index == pathArrayOfStrings.size - 1) break
            folderPath += pathArrayOfStrings[index] + "/"
        }
        return folderPath
    }

    // this private function have made using information above
    // to cut out particular action from the getGalleryFolders
    private fun getImagesInFolder(
        context: Context,
        folderPath: String?
    ): MutableList<GalleryPicture> {

        val columns = arrayOf(
            Images.Media.DATA,
            Images.Media._ID
        )

        val images = mutableListOf<GalleryPicture>()

        /** order data by date */
        val orderBy = Images.Media.DATE_TAKEN

        /** This cursor will hold the result of the query
         * and get all data in Cursor by sorting in descending order */
        val cursor = context.contentResolver.query(
            Images.Media.EXTERNAL_CONTENT_URI,
            columns,
            null,
            null,
            "$orderBy DESC"
        )

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val dataColumnIndex = cursor.getColumnIndex(Images.Media.DATA)

                //added id method
                val id =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))

                val picturePath = cursor.getString(dataColumnIndex)
                val pathArrayOfStrings = picturePath.split('/')

                val currentFolderPath = getFolderPath(pathArrayOfStrings)

                // I switch the following code with the code above
//                var currentFolderPath = ""
//                /** building a firstPicturePath to the folder */
//                for (index in pathArrayOfStrings.indices) {
//                    if (index == pathArrayOfStrings.size - 1) break
//                    currentFolderPath += pathArrayOfStrings[index] + "/"
//                }

                /** collecting images in particular folder */
                if (currentFolderPath == folderPath) {

                    /**  have identified the object GalleryPicture */
                    // I replaced 0 with "id"
                    val galleryPicture = GalleryPicture(picturePath, id)

                    /** zeroing a folder and add an element of the list */
                    images.add(galleryPicture)
                }
            } while (cursor.moveToNext())
            cursor.close()
        }
        return images
    }
}