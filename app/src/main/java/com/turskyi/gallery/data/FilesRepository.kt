package com.turskyi.gallery.data

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Images
import android.provider.MediaStore.Images.Media.DATA
import com.turskyi.gallery.models.Folder
import com.turskyi.gallery.models.PictureUri

class FilesRepository {

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
                    val uriImage = Uri.withAppendedPath(Images.Media.EXTERNAL_CONTENT_URI,
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
//        TODO: replace deprecated "DATA"
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
                    //        TODO: replace deprecated "DATA"
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
//        TODO: remove deprecated "DATA"
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
            if (it.moveToFirst()) {
//                TODO: do something
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