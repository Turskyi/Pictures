package com.turskyi.gallery.data

import android.content.Context
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.provider.MediaStore.Images.Media.DATA
import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.GalleryPicture

class FilesRepository {

    /** finds pictures, nothing else */
    fun getGalleryImages(context: Context): MutableList<GalleryPicture> {

        /** Get all columns of type images */
        // TODO: How to get rid of deprecated "DATA" string?
        val columns = arrayOf(
            DATA,
            Images.Media._ID
        )

        /** Create a list of urls pictures */
        val galleryImageUrls: MutableList<GalleryPicture> = mutableListOf()

        /** order data by date */
        val orderBy = Images.Media.DATE_TAKEN

        /** This cursor will hold the result of the query
        and put all data in Cursor by sorting in descending order */
        //TODO: How correctly to get rid of the following warning in "query"?
        val cursor = context.contentResolver.query(
            Images.Media.EXTERNAL_CONTENT_URI,
            columns, null, null, "$orderBy DESC"
        )

        cursor?.let {
            if (it.moveToNext()) {
                do {
                    val dataColumnIndex = it.getColumnIndex(DATA)
                    val id =
                        it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val galleryPicture = GalleryPicture(it.getString(dataColumnIndex), id)
                    galleryImageUrls.add(galleryPicture)
                } while (it.moveToNext())
                it.close()
            }
        }
        return galleryImageUrls
    }

    fun getGalleryFolders(context: Context): MutableSet<GalleryFolder> {

        val columns = arrayOf(
            DATA,
            Images.Media._ID
        )

        /** My set of folders */
        val galleryFolderUrls: MutableSet<GalleryFolder> = mutableSetOf()

        //TODO: Why is this val must be here or I can move it inside the body?
        val galleryFolderPaths: MutableSet<String> = mutableSetOf()

        /** order data by date */
        //TODO: How correctly to get rid of the following warning in "DATE_TAKEN"?
        val orderBy = Images.Media.DATE_TAKEN

        /** This cursor will hold the result of the query
         * and get all data in Cursor by sorting in descending order */
        val cursor = context.contentResolver.query(
            Images.Media.EXTERNAL_CONTENT_URI,
            columns, null, null, "$orderBy DESC"
        )

        cursor?.let {
            //TODO: Why "moveToFirst" and not "moveToNext()"?
            if (it.moveToFirst()) {
                do {
                    val dataColumnIndex = it.getColumnIndex(DATA)
                    val picturePath = it.getString(dataColumnIndex)
                    val pathArrayOfStrings = picturePath.split('/')
                    val folderName = pathArrayOfStrings[pathArrayOfStrings.size - 2]
                    val folderPath = getFolderPath(pathArrayOfStrings)

                    /* if we found copy of the folder path we skip the rest of the function
                      and go to the next picture */
                    if (galleryFolderPaths.contains(folderPath)) continue

                    galleryFolderPaths.add(folderPath)

                    val images = getImagesInFolder(context, folderPath)
                    val galleryFolder = GalleryFolder(folderPath, picturePath, folderName, images)
                    galleryFolderUrls.add(galleryFolder)
                } while (it.moveToNext())
                it.close()
            }
        }
        return galleryFolderUrls
    }

    private fun getFolderPath(pathArrayOfStrings: List<String>): String {
        var folderPath = ""

        /** building a folderPath out of firstPicturePathString in the folder */
        for (index in pathArrayOfStrings.indices) {
            if (index == pathArrayOfStrings.size - 1) break
            folderPath += pathArrayOfStrings[index] + "/"
        }
        return folderPath
    }

    // this private function had been made by using information above
    // to cut out particular action in the getGalleryFolders
    private fun getImagesInFolder(
        context: Context,
        folderPath: String?
    ): MutableList<GalleryPicture> {

        val columns = arrayOf(
            DATA,
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

        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val dataColumnIndex = it.getColumnIndex(DATA)
                    val id =
                        it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val picturePath = it.getString(dataColumnIndex)
                    val pathArrayOfStrings = picturePath.split('/')
                    val currentFolderPath = getFolderPath(pathArrayOfStrings)

                    /** collecting images in particular folder */
                    if (currentFolderPath == folderPath) {
                        val galleryPicture = GalleryPicture(picturePath, id)
                        /** zeroing a folder and add an element of the list */
                        images.add(galleryPicture)
                    }
                } while (it.moveToNext())
                it.close()
            }
        }
        return images
    }
}