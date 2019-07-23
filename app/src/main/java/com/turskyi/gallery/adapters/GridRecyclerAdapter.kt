package com.turskyi.gallery.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.DetailActivity
import com.turskyi.gallery.FileLiveSingleton
import com.turskyi.gallery.R
import com.turskyi.gallery.model.MyFile

class GridRecyclerAdapter(private val aContext: Context, private val aFileList: ArrayList<MyFile?>) : RecyclerView.Adapter<GridRecyclerAdapter.FileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val viewLarge = LayoutInflater.from(parent.context).inflate(R.layout.picture_item, parent, false)
        return FileViewHolder(viewLarge)
    }

    override fun getItemCount(): Int {
        return aFileList.size
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        aFileList[position]?.let { holder.bindView(it,aContext) }
    }

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fileNameTV: TextView = itemView.findViewById(R.id.file_name)
        var aFileIV: ImageView = itemView.findViewById(R.id.folder_image_view_preview)
        fun bindView(aFile: MyFile, aContext: Context) {
            fileNameTV.text = aFile.name
            itemView.setOnClickListener{
                FileLiveSingleton.getInstance().setPath(aFile.path)
            }

            if(aFile.extension in listOf("jpeg", "png", "jpg", "webp", "JPEG", "PNG", "JPG")) {
                val aBitmap = BitmapFactory.decodeFile(aFile.path)
                aFileIV.setImageBitmap(aBitmap)

                //to use for opening the picture
                itemView.setOnClickListener {
                    val anIntent = Intent(aContext, DetailActivity::class.java)
                    anIntent.putExtra("File", aFile.path)
                    aContext.startActivity(anIntent)
                }
            }
        }
    }
}



