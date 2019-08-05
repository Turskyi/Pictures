package com.turskyi.gallery.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.FileLiveSingleton
import com.turskyi.gallery.R
import com.turskyi.gallery.fragments.DetailFragment
import com.turskyi.gallery.models.MyFile
import com.turskyi.gallery.models.ViewTypes
import com.turskyi.gallery.models.ViewTypes.*

class FileRecyclerViewAdapter(
    //TODO меморі лік через контекст
    private val aContext: Context,
    //TODO в котліні не треба створювати функцію для заповнення масива, під його оголошенням можна написати
    // set(value) {
    //  field = value
    //  notifyDataSetChanged()
    //  }
    //
    private var listFile: ArrayList<MyFile?>,
    private var isGridEnum: ViewTypes = LINEAR,
    private var numberOfChecked: Int = 0
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /** set  the viewType */
    override fun getItemViewType(position: Int): Int {
        return when (isGridEnum) {
            GRID -> GRID.id
            LOADING -> LOADING.id
            else -> LINEAR.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LINEAR.id -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
                ListViewHolder(view)
            }
            ViewTypes.LOADING.id -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
                MyLoadingHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.picture_item, parent, false)
                ListViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return listFile.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ListViewHolder)?.bindView(listFile[position]!!, aContext)
    }

    fun setNewList(listFile: ArrayList<MyFile?>) {
        this.listFile = listFile
        notifyDataSetChanged()
    }

    fun addNewFiles(listFile: List<MyFile?>) {
        if (listFile.isNotEmpty()) {
            for (aFile in listFile) {
                this.listFile.add(aFile)
            }
            notifyDataSetChanged()
        }
    }

    fun changeViewType() {
        isGridEnum = if (isGridEnum.id == ViewTypes.LINEAR.id) {
            GRID
        } else {
            LINEAR
        }
        notifyDataSetChanged()
    }

    fun getAllChecked(): ArrayList<MyFile?> {
        val checkedFiles: ArrayList<MyFile?> = ArrayList()
        for (aFile in listFile) {
            if (aFile?.isChecked!!) {
                numberOfChecked++
                checkedFiles.add(aFile)
                notifyDataSetChanged()
            }
        }
        notifyDataSetChanged()
        return checkedFiles
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //TODO холдер є тільки для доступу до айтемів, в ньому не відбувається жодних оголошень, бо вони мінливі
        // занесення даних повинне відбуватися в он бінд, коли холдер повертається на екран
        // крім того об'єкти холдери можуть використовуватися повторно з іншими ресурсами. роблячи так ти блокуєш цю функцію
        private val fileNameTV: TextView = itemView.findViewById(R.id.file_name)
        private val previewIV: ImageView = itemView.findViewById(R.id.file_iv_preview)

        /* I left this object to perform onLongClickListener otherwise it is not gonna work */
        private val selectedImage: ImageView = itemView.findViewById(R.id.selected_image)

        fun bindView(aFile: MyFile, aContext: Context) {
            fileNameTV.text = aFile.name

            itemView.setOnLongClickListener(imageLongClickListener)

            if (aFile.extension in listOf("jpeg", "png", "jpg", "webp", "JPEG", "PNG", "JPG")) {
                val aBitmap = BitmapFactory.decodeFile(aFile.path)
                previewIV.scaleType = ImageView.ScaleType.CENTER_CROP

                previewIV.setImageBitmap(aBitmap)

                previewIV.setOnClickListener {
                    FileLiveSingleton.getInstance().setPath(aFile.path)
                }

                /** to use for opening the picture */
                itemView.setOnClickListener {
                    val anIntent = Intent(aContext, DetailFragment::class.java)
                    anIntent.putExtra("File", aFile.path)
                    aContext.startActivity(anIntent)
                }
            } else {

                /** if image exists in folder */
                aFile.imageFile?.let {
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

        private var imageLongClickListener: OnLongClickListener = OnLongClickListener {
            if (selectedImage.visibility == View.INVISIBLE) {
                selectedImage.visibility = View.VISIBLE
            } else {
                selectedImage.visibility = View.INVISIBLE
            }
            true
        }
    }

    class MyLoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
