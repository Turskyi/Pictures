package com.turskyi.gallery.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.R
import com.turskyi.gallery.DetailActivity
import java.io.File


class RecyclerAdapter(
    private val aContext: Context,
    private val aFileList: List<File>
) : RecyclerView.Adapter<RecyclerAdapter.FileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item,
            parent, false
        )
        return FileViewHolder(view)
    }

    override fun getItemCount(): Int {
        return aFileList.size
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
holder.bindView(aFileList[position])

//        holder.aFolder.setImageResource(aFileList[position])
//        holder.aFolder.setOnClickListener {
//            val anIntent = Intent(aContext, DetailActivity::class.java)
//            anIntent.putExtra("Image", aFileList[holder.adapterPosition])
//            aContext.startActivity(anIntent)
//        }

    }



        class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val fileNameTV: TextView = itemView.findViewById(R.id.file_name)
//            var aFolder: ImageView = itemView.findViewById(R.id.image_view_preview)
            fun bindView(file:File) {
    fileNameTV.text = file.name
}
        }
    }



