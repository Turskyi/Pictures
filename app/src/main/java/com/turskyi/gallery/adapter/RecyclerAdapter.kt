package com.turskyi.gallery.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.R
import com.turskyi.gallery.DetailActivity


class RecyclerAdapter(
    private val aContext: Context,
    private val aFolderList: Array<Int>
) : RecyclerView.Adapter<RecyclerAdapter.FolderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item,
            parent, false
        )
        return FolderViewHolder(view,aFolderList)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
//        holder.bindView(aFolderList[holder.adapterPosition], aContext)
        holder.aFolder.setImageResource(aFolderList[position])
        holder.aFolder.setOnClickListener {
            val anIntent = Intent(aContext, DetailActivity::class.java)
            anIntent.putExtra("Image", aFolderList[holder.adapterPosition])
            aContext.startActivity(anIntent)
        }
    }

        override fun getItemCount(): Int {
            return aFolderList.size
        }

        class FolderViewHolder(itemView: View, private val aFolderList: Array<Int>) : RecyclerView.ViewHolder(itemView) {

            var aFolder: ImageView = itemView.findViewById(R.id.ivFolder)

//            fun bindView(holder: FolderViewHolder,aContext: Context) {
//                itemView.setOnClickListener {
//                    val anIntent = Intent(aContext, DetailActivity::class.java)
//                    anIntent.putExtra("Image", aFolderList[holder.adapterPosition])
//                    aContext.startActivity(anIntent)
//                }
//            }
        }
    }



