package com.turskyi.gallery.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.SyncStateContract
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
import com.turskyi.gallery.data.Constants
import com.turskyi.gallery.fragments.PicturesInFolderFragment
import com.turskyi.gallery.interfaces.OnFolderClickListener
import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.ViewType
import java.io.File

//TODO в котліні не треба створювати функцію для заповнення масива, під його оголошенням можна написати
// set(value) {
//  field = value
//  notifyDataSetChanged() }

class FoldersViewAdapter(
    private var foldersList: MutableSet<GalleryFolder>?,
    private val onFolderClickListener: OnFolderClickListener?
) : RecyclerView.Adapter<FoldersViewAdapter.FolderViewHolder>() {

//    private var number = 0
//        set(number) {
//            field = number
//        }
//        get() = field

    private var viewType: ViewType = ViewType.GRID

    /** set  the viewTypes */
    override fun getItemViewType(position: Int): Int {
        return if (viewType == ViewType.GRID) ViewType.GRID.id
        else ViewType.LINEAR.id
    }

    /** switch between layouts */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        return if (viewType == ViewType.LINEAR.id) {
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
                onFolderClickListener?.addOnLongClick(foldersList?.elementAt(position)!!)
            } else {
                holder.selectedFolder.visibility = View.INVISIBLE
                onFolderClickListener?.removeOnLongClick(
                    foldersList?.elementAt(position)!!,
                    viewType
                )
            }
            true
        }

        holder.itemView.setOnLongClickListener {
            if (holder.selectedFolder.visibility == View.INVISIBLE) {
                holder.selectedFolder.visibility = View.VISIBLE
                onFolderClickListener?.addOnLongClick(foldersList?.elementAt(position)!!)
            } else {
                holder.selectedFolder.visibility = View.INVISIBLE
                onFolderClickListener?.removeOnLongClick(
                    foldersList?.elementAt(position)!!,
                    viewType
                )
            }
            true
        }
        holder.bindView(foldersList!!.elementAt(position))
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
            /** Makes the cover of a folder with a picture */
            val file = File(galleryFolder.firstPicturePath)
            folderNameTV.text = galleryFolder.name
            val uri: Uri = Uri.fromFile(file)
            Glide.with(context).load(uri).into(previewIV)
            previewIV.setImageBitmap(BitmapFactory.decodeFile(galleryFolder.firstPicturePath))

            previewIV.setOnClickListener {
                val fragmentManager: FragmentTransaction =
                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val picturesInFolderFragment = PicturesInFolderFragment(galleryFolder)
                fragmentManager
                    .replace(R.id.container, picturesInFolderFragment, Constants.TAG_PICS_IN_FOLDER)
                    .addToBackStack(Constants.TAG_PICS_IN_FOLDER).commit()
            }

            previewIV.setOnClickListener {
                val fragmentManager: FragmentTransaction =
                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val picturesInFolderFragment = PicturesInFolderFragment(galleryFolder)
                fragmentManager
                    .replace(R.id.container, picturesInFolderFragment, Constants.TAG_PICS_IN_FOLDER)
                    .addToBackStack(Constants.TAG_PICS_IN_FOLDER).commit()
            }
        }
    }

    /** This method to update the layout */
    fun changeViewType() {
        viewType = if (viewType.id == ViewType.LINEAR.id) {
            ViewType.GRID
        } else {
            ViewType.LINEAR
        }
        notifyDataSetChanged()
    }
}
