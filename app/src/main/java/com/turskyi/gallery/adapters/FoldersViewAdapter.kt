package com.turskyi.gallery.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.turskyi.gallery.R
import com.turskyi.gallery.fragments.PicturesInFolderFragment
import com.turskyi.gallery.interfaces.OnFolderClickListener
import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.ViewTypes
import com.turskyi.gallery.models.ViewTypes.GRID
import com.turskyi.gallery.models.ViewTypes.LINEAR
import java.io.File

//TODO в котліні не треба створювати функцію для заповнення масива, під його оголошенням можна написати
// set(value) {
//  field = value
//  notifyDataSetChanged() }

class FoldersViewAdapter(
    private var foldersList: MutableSet<GalleryFolder>?,
    private val onFolderClickListener: OnFolderClickListener?) :
    RecyclerView.Adapter<FoldersViewAdapter.FolderViewHolder>() {

    private var viewType: ViewTypes = GRID

    /** set  the viewType */
    override fun getItemViewType(position: Int): Int {
        return if (viewType == GRID) GRID.id
        else LINEAR.id
    }

    /** switch between layouts */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        return if (viewType == LINEAR.id) {
            val view = LayoutInflater.from(parent.context)
                .inflate(com.turskyi.gallery.R.layout.folder_list_item, parent, false)
            FolderViewHolder(view, parent.context)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(com.turskyi.gallery.R.layout.folder_item, parent, false)
            FolderViewHolder(view, parent.context)
        }
    }

    override fun getItemCount(): Int {
        foldersList ?: run {
            return 0
        }
        return foldersList!!.size
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {

        /** making "check sign visible and invisible onLongClick */
        holder.previewIV.setOnLongClickListener {
            if (holder.selectedFolder.visibility == View.INVISIBLE) {
                holder.selectedFolder.visibility = View.VISIBLE
//                holder.selectedFolder.isSelected
                onFolderClickListener?.addOnClick(foldersList?.elementAt(position)!!)
            } else {
                holder.selectedFolder.visibility = View.INVISIBLE
//                holder.selectedFolder.isSelected
                onFolderClickListener?.removeOnClick(foldersList?.elementAt(position)!!)
            }
            true
        }
        foldersList!!.elementAt(position).let { holder.bindView(it) }
    }

    class FolderViewHolder(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(itemView) {

        //TODO холдер є тільки для доступу до айтемів, в ньому не відбувається жодних оголошень,
        // бо вони мінливі
        // занесення даних повинне відбуватися в он бінд, коли холдер повертається на екран
        // крім того об'єкти холдери можуть використовуватися повторно з іншими ресурсами.
        // роблячи так ти блокуєш цю функцію
        private val folderNameTV: TextView =
            itemView.findViewById(com.turskyi.gallery.R.id.folderName)
        val previewIV: ImageView =
            itemView.findViewById(com.turskyi.gallery.R.id.folderPreviewIV)
        val selectedFolder: ImageView =
            itemView.findViewById(com.turskyi.gallery.R.id.selectedFolder)

        fun bindView(galleryFolder: GalleryFolder) {
            val file = File(galleryFolder.firstPicturePath)
            folderNameTV.text = galleryFolder.name
            val uri: Uri = Uri.fromFile(file)
            Glide.with(context).load(uri).into(previewIV)

            /** Makes the cover of a folder with a picture */
            previewIV.setImageBitmap(BitmapFactory.decodeFile(galleryFolder.firstPicturePath))

            previewIV.setOnClickListener {
                val fragmentManager: FragmentTransaction =
                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val picturesInFolderFragment = PicturesInFolderFragment(galleryFolder)
                fragmentManager.replace(R.id.container, picturesInFolderFragment).commit()
            }

            /* the method bellow I am going to use later for unknown yet reason */
//            itemView.setOnClickListener {
//                FileLiveSingleton.getInstance().setPath(galleryFolder.firstPicturePath)
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
}
