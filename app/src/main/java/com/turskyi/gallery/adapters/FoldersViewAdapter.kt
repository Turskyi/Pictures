package com.turskyi.gallery.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.turskyi.gallery.PicturesInFolderActivity
import com.turskyi.gallery.R
import com.turskyi.gallery.data.Constants
import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.ViewTypes
import com.turskyi.gallery.models.ViewTypes.GRID
import com.turskyi.gallery.models.ViewTypes.LINEAR
import java.io.File

//TODO в котліні не треба створювати функцію для заповнення масива, під його оголошенням можна написати
// set(value) {
//  field = value
//  notifyDataSetChanged() }

class FoldersViewAdapter(private var listFolder: MutableSet<GalleryFolder>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var viewType: ViewTypes = GRID

    /** set  the viewType */
    override fun getItemViewType(position: Int): Int {
        return if (viewType == GRID) GRID.id
        else LINEAR.id
    }

    /** switch between layouts */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {
        return if (viewType == LINEAR.id) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.folder_list_item, parent, false)
            FolderViewHolder(view, parent.context)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.folder_item, parent, false)
            FolderViewHolder(view, parent.context)
        }
    }

    override fun getItemCount(): Int {
        listFolder ?: run {
            return 0
        }
        return listFolder!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        (holder as? FolderViewHolder)?.bindView(listFolder!![position])
        // I replaced this line with the code above I hope that is correct
        listFolder!!.elementAt(position).let { (holder as? FolderViewHolder)?.bindView(it) }
    }

    /* I do not know how to get all checked folders yet */
    fun getAllChecked(): MutableSet<GalleryFolder?> {
        var numberOfChecked = 0
        val checkedFiles: MutableSet<GalleryFolder?> = mutableSetOf()
        for (aFile in this.listFolder!!) {
            if (aFile.isSelected) {
                numberOfChecked++
                checkedFiles.add(aFile)
                notifyDataSetChanged()
            }
        }
        notifyDataSetChanged()
        return checkedFiles
    }

    class FolderViewHolder(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(itemView) {

        //TODO холдер є тільки для доступу до айтемів, в ньому не відбувається жодних оголошень, бо вони мінливі
        // занесення даних повинне відбуватися в он бінд, коли холдер повертається на екран
        // крім того об'єкти холдери можуть використовуватися повторно з іншими ресурсами. роблячи так ти блокуєш цю функцію
        private val folderNameTV: TextView = itemView.findViewById(R.id.folderName)
        private val previewIV: ImageView = itemView.findViewById(R.id.folderPreviewIV)

        fun bindView(galleryFolder: GalleryFolder) {
            val file = File(galleryFolder.path)
            folderNameTV.text = galleryFolder.name
            val uri: Uri = Uri.fromFile(file)
            Glide.with(context).load(uri).into(previewIV)
            val selectedFolder: ImageView = itemView.findViewById(R.id.selectedFolder)
            /** Makes the cover of a folder with a picture */
            previewIV.setImageBitmap(BitmapFactory.decodeFile(galleryFolder.path))

            previewIV.setOnClickListener {

                // supposed to send to PicturesInFolderFragment
//                val intent = Intent(context, PicturesInFolderFragment::class.java)

                //some kind of method to send data to second activity, but I do not know how to use it
//                startActivityForResult(
//                    context.applicationContext as Activity, intent, 1, /* what should be here?*/)

                // temporary method to send to PicturesInFolderActivity
                val intent = Intent(context, PicturesInFolderActivity::class.java)
                intent.putExtra(Constants.KEY_WORD_PATH, galleryFolder.path)
                context.startActivity(intent)

                //attempt to make DetailedFragment,
                // but it does not want to implement supportFragmentManager yet
//                val detailFragment = PicturesInFolderFragment()
//                detailFragment.arguments = intent.extras
//                val fragmentManager = getChildFragmentManager.beginTransaction()
//                fragmentManager.add(android.R.id.content, detailFragment).commit()
            }

            /** making "check sign visible and invisible onLongClick */
            previewIV.setOnLongClickListener {
                if (selectedFolder.visibility == View.INVISIBLE) {
                    selectedFolder.visibility = View.VISIBLE
                } else {
                    selectedFolder.visibility = View.INVISIBLE
                }
                true
            }

            /* the method bellow I am going to use later for unknown yet reason */
//            itemView.setOnClickListener {
//                FileLiveSingleton.getInstance().setPath(galleryFolder.path)
//            }
        }
    }

    /** This method to update the layout */
    fun changeViewType() {
        viewType = if (viewType.id == ViewTypes.LINEAR.id) {
            GRID
        } else {
            LINEAR
        }
        notifyDataSetChanged()
    }

    //some kind of method to send data to second activity, but I do not know how to use it
//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//             //?
//            }
//        }
//    }
}
