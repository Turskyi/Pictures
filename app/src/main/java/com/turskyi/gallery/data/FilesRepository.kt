package com.turskyi.gallery.data

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Images
import android.provider.MediaStore.Images.Media.DATA
import com.turskyi.gallery.models.Folder
import com.turskyi.gallery.models.Picture
import com.turskyi.gallery.models.PictureUri

class FilesRepository {

    fun getImagePathList(from: Int, to: Int, context: Context): List<Picture> {
        /** Create a list of urls pictures */
        val listOfImages = mutableListOf<Picture>()

        /** Get all columns of type images */
        val columns = arrayOf(DATA, Images.Media._ID)

        /** order data by date */
        val orderBy =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                Images.Media.DATE_TAKEN
            } else {
                Images.Media._ID
            }

        /** This cursor will hold the result of the query
        and put all data in Cursor by sorting in descending order */
        val cursor = context.contentResolver.query(
            Images.Media.EXTERNAL_CONTENT_URI,
            columns, null, null, "$orderBy DESC"
        )
        cursor?.let { file ->
            for (i in from until to + from) {
                if (file.moveToNext() && i < file.columnCount - 1) {
                    do {
                        val dataColumnIndex = file.getColumnIndex(DATA)
                        val id =
                            file.getLong(file.getColumnIndexOrThrow(Images.Media._ID))
                        val galleryPicture = Picture(file.getString(dataColumnIndex), id)
                        listOfImages.add(galleryPicture)
                    } while (file.moveToNext())
                }
            }
        }
        cursor?.close()
        return listOfImages
    }

    fun getDataOfImageList(from: Int, to: Int, context: Context): List<PictureUri> {

        val listOfImages = mutableListOf<PictureUri>()
        val columns = arrayOf(Images.Media._ID)

        val orderBy =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) Images.Media.DATE_TAKEN
            else Images.Media._ID

        /** This cursor will hold the result of the query
        and put all data in Cursor by sorting in descending order */
        val cursor = context.contentResolver.query(
            Images.Media.EXTERNAL_CONTENT_URI,
            columns, null, null, "$orderBy DESC"
        )
        cursor?.let { fileCursor ->
            val columnIndexID: Int = fileCursor.getColumnIndexOrThrow(Images.Media._ID)
            for (i in from until to + from) {
                while (fileCursor.moveToNext() && i < fileCursor.columnCount) {
                val id = fileCursor.getLong(columnIndexID)
                    val uriImage =
                        Uri.withAppendedPath(
                            Images.Media.EXTERNAL_CONTENT_URI,
                    "" + id)
                val galleryPicture = PictureUri(uriImage)
                        listOfImages.add(galleryPicture)
                }
            }
            cursor.close()
        }
        return listOfImages
    }

    fun getDataOfFolderList(from: Int, to: Int, context: Context): List<Folder> {
        val listOfFolders = mutableListOf<Folder>()
        val setOfFolders = getSetOfFolders(context)
        for (i in from until to + from) {
            if (i < setOfFolders.size) {
                listOfFolders.add(setOfFolders.elementAt(i))
            }
        }
        return listOfFolders
    }


    private fun getSetOfFolders(context: Context): MutableSet<Folder> {
      val columns = arrayOf(DATA, Images.Media._ID)

        /** My set of folders */
        val folderUrls: MutableSet<Folder> = mutableSetOf()

        val galleryFolderPaths: MutableSet<String> = mutableSetOf()

        /** order data by date */
        val orderBy =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                Images.Media.DATE_TAKEN
            } else {
                Images.Media.DATE_ADDED
            }

        /** This cursor will hold the result of the query
         * and get all data in Cursor by sorting in descending order */
        val cursor = context.contentResolver.query(
            Images.Media.EXTERNAL_CONTENT_URI,
            columns, null, null, "$orderBy DESC"
        )
        cursor?.let {
            /* checking if first box of the array is not null */
            if (it.moveToFirst()) {
                do {
               val dataColumnIndex = it.getColumnIndex(DATA)
                    val pictureInFolderPath = it.getString(dataColumnIndex)
                    val pathArrayOfStrings = pictureInFolderPath.split('/')
                    val folderName = pathArrayOfStrings[pathArrayOfStrings.size - 2]
                    val folderPath = getFolderPath(pathArrayOfStrings)

                    /** If we found copy of the folder path we skip the rest of the function
                    and go to the next picture */
                    if (galleryFolderPaths.contains(folderPath)) continue

                    galleryFolderPaths.add(folderPath)

                    val galleryFolder = Folder(folderPath, pictureInFolderPath, folderName)
                    folderUrls.add(galleryFolder)
                    // checking if next box of the array is not null
                } while (it.moveToNext())
            }
        }
        cursor?.close()
        return folderUrls
    }

  private fun getFolderPath(pathArrayOfStrings: List<String>): String {
        var folderPath = ""

        /** Building a folderPath out of firstPicturePathString in the folder */
        for (index in pathArrayOfStrings.indices) {
            if (index == pathArrayOfStrings.size - 1) break
            folderPath += pathArrayOfStrings[index] + "/"
        }
        return folderPath
    }

    fun getSetOfImagesInFolder(
        context: Context
    ): MutableSet<PictureUri> {
      val columns = arrayOf(
            DATA,
            Images.Media._ID
        )
        val images = mutableSetOf<PictureUri>()

        /** order data by date */
        val orderBy =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                Images.Media.DATE_TAKEN
            } else {
                Images.Media.DEFAULT_SORT_ORDER
            }
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
            @Suppress("ControlFlowWithEmptyBody")
            if (it.moveToFirst()) {
//                do {
//                    val dataColumnIndex = it.getColumnIndex(DATA)
//                    val id =
//                        it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
//                    val picturePath = it.getString(dataColumnIndex)
//                    val pathArrayOfStrings = picturePath.split('/')
//                    val currentFolderPath = getFolderPath(pathArrayOfStrings)
//
//                    /** collecting images in particular folder */
//                    if (currentFolderPath == folderPath) {
//                        val galleryPicture = PictureUri(picturePath, id)
//                        /** zeroing a folder and add an element of the list */
//                        images.add(galleryPicture)
//                    }
//                } while (it.moveToNext())
            }
        }
        cursor?.close()
        return images
    }

    fun getDataOfImagesInFolderList(
        from: Int,
        to: Int,
        context: Context
    ): List<PictureUri> {
        /** Create a list of urls pictures */
        val listOfImages = mutableListOf<PictureUri>()
        val setOfImages = getSetOfImagesInFolder(context)
        for (i in from until to + from) {
            if (i < setOfImages.size) {
                listOfImages.add(setOfImages.elementAt(i))
            }
        }
        return listOfImages
    }
}