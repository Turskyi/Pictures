package com.turskyi.gallery.adapters

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.turskyi.gallery.R
import com.turskyi.gallery.data.DiffUtilComparators.FOLDERS_DIFF_CALLBACK
import com.turskyi.gallery.data.GalleryConstants
import com.turskyi.gallery.fragments.PicturesInFolderFragment
import com.turskyi.gallery.interfaces.OnFolderLongClickListener
import com.turskyi.gallery.models.Folder
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewholders.FolderListViewHolder
import java.io.File

class FolderListAdapter(
    private val onFolderLongClickListener: OnFolderLongClickListener
) : PagedListAdapter<Folder, RecyclerView.ViewHolder>(FOLDERS_DIFF_CALLBACK) {

    private var viewType: ViewType = ViewType.LINEAR

    /**
     * @Description switch between layouts
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FolderListViewHolder(parent)
    }

    /**
     * @Description making "check" sign visible and invisible onLongClick.
     * Makes the cover of a folder with a picture
     * */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
      if (holder is FolderListViewHolder) {
            /** making "check sign visible and invisible onLongClick */
            holder.ivFolderListItemPreview.setOnLongClickListener {
                if (holder.ivSelectedFolder.visibility == View.INVISIBLE) {
                    holder.ivSelectedFolder.visibility = View.VISIBLE
                    onFolderLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.ivSelectedFolder.visibility = View.INVISIBLE
                    onFolderLongClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }

            holder.itemView.setOnLongClickListener {
                if (holder.ivSelectedFolder.visibility == View.INVISIBLE) {
                    holder.ivSelectedFolder.visibility = View.VISIBLE
                    onFolderLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.ivSelectedFolder.visibility = View.INVISIBLE
                    onFolderLongClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }

            val file = File(getItem(position)!!.firstPicturePath)
            holder.tvListItemName.text = getItem(position)!!.name
            val uri: Uri = Uri.fromFile(file)
            Glide.with(holder.itemView.context).load(uri).into(holder.ivFolderListItemPreview)

            holder.ivFolderListItemPreview.setOnClickListener {
                val fragmentManager: FragmentTransaction =
                    (holder.itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val picturesInFolderFragment =
                    PicturesInFolderFragment(getItem(position)!!)
                fragmentManager
                    .replace(R.id.container, picturesInFolderFragment, GalleryConstants.TAG_PICS_IN_FOLDER)
                    .addToBackStack(GalleryConstants.TAG_PICS_IN_FOLDER).commit()
            }

            holder.itemView.setOnClickListener {
                val fragmentManager: FragmentTransaction =
                    (holder.itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val picturesInFolderFragment =
                    PicturesInFolderFragment(getItem(position)!!)
                fragmentManager
                    .replace(R.id.container, picturesInFolderFragment, GalleryConstants.TAG_PICS_IN_FOLDER)
                    .addToBackStack(GalleryConstants.TAG_PICS_IN_FOLDER).commit()
            }
        }
    }

    /**
     * @Description updates the layout
     */
    fun changeViewType() {
        viewType = if (viewType == ViewType.LINEAR) {
            ViewType.GRID
        } else {
            ViewType.LINEAR
        }
        notifyItemRangeChanged(0, itemCount)
    }
}
