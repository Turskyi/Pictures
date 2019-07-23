package com.turskyi.gallery.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.BitmapFactory
import android.view.View.OnLongClickListener
import com.turskyi.gallery.DetailActivity
import com.turskyi.gallery.FileLiveSingleton
import com.turskyi.gallery.R
import com.turskyi.gallery.model.MyFile

class MyRecyclerViewAdapter(private val aContext: Context, private var listFile: ArrayList<MyFile?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

//  class  ViewHolder1 : RecyclerView.Adapter<ListRecyclerAdapter.FileViewHolder>() {
//
//       ViewHolder1(var itemView: View ){
//        }
//    }

    //  class  ViewHolder2 : RecyclerView.Adapter<ListRecyclerAdapter.FileViewHolder>() {
//
//       ViewHolder1(var itemView: View ){
//        }
//    }

    // set viewType
    override fun getItemViewType(position: Int): Int {
        return if (listFile[position] == null) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            ListViewHolder(view)

        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            MyLoadingHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return listFile.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ListViewHolder) {
            holder.bindView(listFile[position]!!, aContext)
        } else {

        }
    }

    fun addNewFiles(listFile: List<MyFile?>) {
        if (listFile.isNotEmpty()) {
            for (aFile in listFile) {
                this.listFile.add(aFile)
            }
            notifyDataSetChanged()
        }
    }

//    fun getAllChecked() : ArrayList<MyFile> {
////         TODO return variable
////         TODO for with filter
////         TODO return variable
//
//         var checkedFiles = ArrayList<MyFile?> {
//             for (aFile in listFile) {
//                 if (aFile?.isChecked!!) {
//             }
//         }
//    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val fileNameTV: TextView = itemView.findViewById(R.id.file_name)
        private val previewIV: ImageView = itemView.findViewById(R.id.list_iv_preview)
        private val selectedImage: ImageView = itemView.findViewById(R.id.selected_image)
        fun bindView(aFile: MyFile, aContext: Context) {
            fileNameTV.text = aFile.name

            itemView.setOnLongClickListener (firstListener)


            if (aFile.extension in listOf("jpeg", "png", "jpg", "webp", "JPEG", "PNG", "JPG")) {
                val aBitmap = BitmapFactory.decodeFile(aFile.path)
                previewIV.scaleType = ImageView.ScaleType.CENTER_CROP

                previewIV.setImageBitmap(aBitmap)

                previewIV.setOnClickListener {
                    FileLiveSingleton.getInstance().setPath(aFile.path)
                }

                //to use for opening the picture
                itemView.setOnClickListener {
                    val anIntent = Intent(aContext, DetailActivity::class.java)
                    anIntent.putExtra("File", aFile.path)
                    aContext.startActivity(anIntent)
                }

            } else {
                // if image exist in folder
                if (aFile.imageFile != null) {
                    val path: String = aFile.imageFile.path
                    val myBitmap = BitmapFactory.decodeFile(path)
                    previewIV.scaleType = ImageView.ScaleType.CENTER_CROP
                    previewIV.setImageBitmap(myBitmap)
                }

                itemView.setOnClickListener {
                    FileLiveSingleton.getInstance().setPath(aFile.path)
                }
            }

        }


        private var firstListener: OnLongClickListener = OnLongClickListener {
            selectedImage.visibility = View.VISIBLE
            itemView.setOnLongClickListener(secondListener)
            true
        }

        private var secondListener: OnLongClickListener = OnLongClickListener {
            selectedImage.visibility = View.INVISIBLE

            itemView.setOnLongClickListener(firstListener)
            true
        }

    }

    class MyLoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
