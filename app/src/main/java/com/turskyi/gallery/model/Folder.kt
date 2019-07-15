package com.turskyi.gallery.model

data class Folder(val path: Int)

//@Entity(tableName = Folder.TABLE_NAME)
//data class Folder(
//    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = COLUMN_ID) var id: Int,
//    @ColumnInfo(name = COLUMN_NAME) var path: Int
//){
//    companion object{
//        const val TABLE_NAME = "Images"
//        const val COLUMN_ID = "id"
//        const val COLUMN_NAME = "image_path"
//    }
//}