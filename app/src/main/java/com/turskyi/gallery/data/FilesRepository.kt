package com.turskyi.gallery.data

import android.content.ContentUris
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
            if (cursor.moveToNext())
                do {
                    val dataColumnIndex = cursor.getColumnIndex(Images.Media.DATA)
                    val id =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val galleryPicture = GalleryPicture(cursor.getString(dataColumnIndex))
                    galleryPicture.uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    galleryImageUrls.add(galleryPicture)
                } while (cursor.moveToNext())
            cursor.close()
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
        val galleryFolderNames: MutableSet<String> = mutableSetOf()

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
                val pathArrayOfStrings = cursor.getString(dataColumnIndex).split('/')
                val folderName = pathArrayOfStrings[pathArrayOfStrings.size - 2]
                if (galleryFolderNames.contains(folderName)) break
                galleryFolderNames.add(folderName)
                galleryFolderUrls.add(
                    GalleryFolder(
                        cursor.getString(dataColumnIndex), folderName
                    )
                )
            } while (cursor.moveToNext())
            cursor.close()
        }
        return galleryFolderUrls
    }

    // I do not know how to find pictures in folder
    fun getImagesInFolder(context: Context): MutableList<GalleryPicture> {

        /** Get all columns of files with image type */
        val columns = arrayOf(
            Images.Media.DATA,
            Images.Media._ID
        )

        /** Create a list of pictures in Folder */
        val imagesInFolderUrls: MutableList<GalleryPicture> = mutableListOf()

        /** order data by date */
        val orderBy = Images.Media.DATE_TAKEN

        /** This cursor will hold the result of the query
         * and get all data in Cursor by sorting in descending order */
        val cursor = context.contentResolver.query(
            Images.Media.EXTERNAL_CONTENT_URI, columns, null,
            null, "$orderBy DESC"
        )
        cursor?.let {
            if (cursor.moveToNext())
                do {
                    val dataColumnIndex = cursor.getColumnIndex(Images.Media.DATA)
                    imagesInFolderUrls.add(GalleryPicture(cursor.getString(dataColumnIndex)))
                } while (cursor.moveToNext())
            cursor.close()
        }
        return imagesInFolderUrls
    }
}