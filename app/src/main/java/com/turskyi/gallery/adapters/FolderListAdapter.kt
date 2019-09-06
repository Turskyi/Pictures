package com.turskyi.gallery.adapters

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.interfaces.OnFolderClickListener
import com.turskyi.gallery.models.GalleryFolder
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewholders.FolderGridViewHolder
import com.turskyi.gallery.viewholders.FolderListViewHolder

class FolderListAdapter(
    private val onFolderClickListener: OnFolderClickListener
) : PagedListAdapter<GalleryFolder, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<GalleryFolder> =
            object : DiffUtil.ItemCallback<GalleryFolder>() {
                override fun areItemsTheSame(
                    oldItem: GalleryFolder,
                    newItem: GalleryFolder
                ): Boolean {
                    return oldItem.folderPath == newItem.folderPath
                }

                override fun areContentsTheSame(
                    oldItem: GalleryFolder,
                    newItem: GalleryFolder
                ): Boolean {
                    return oldItem.firstPicturePath == newItem.firstPicturePath
                }
            }
    }

    private var viewType: ViewType = ViewType.LINEAR

    /** switch between layouts */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (this.viewType == ViewType.GRID) {
            FolderGridViewHolder(parent)
        } else {
            FolderListViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FolderGridViewHolder) {
            /** making "check sign visible and invisible onLongClick */
            holder.previewIV.setOnLongClickListener {
                if (holder.selectedFolder.visibility == View.INVISIBLE) {
                    holder.selectedFolder.visibility = View.VISIBLE
                    onFolderClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedFolder.visibility = View.INVISIBLE
                    onFolderClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }

            holder.itemView.setOnLongClickListener {
                if (holder.selectedFolder.visibility == View.INVISIBLE) {
                    holder.selectedFolder.visibility = View.VISIBLE
                    onFolderClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedFolder.visibility = View.INVISIBLE
                    onFolderClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }
            holder.bindView(getItem(position)!!)
        } else if (holder is FolderListViewHolder) {
            /** making "check sign visible and invisible onLongClick */
            holder.previewIV.setOnLongClickListener {
                if (holder.selectedFolder.visibility == View.INVISIBLE) {
                    holder.selectedFolder.visibility = View.VISIBLE
                    onFolderClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedFolder.visibility = View.INVISIBLE
                    onFolderClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }

            holder.itemView.setOnLongClickListener {
                if (holder.selectedFolder.visibility == View.INVISIBLE) {
                    holder.selectedFolder.visibility = View.VISIBLE
                    onFolderClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedFolder.visibility = View.INVISIBLE
                    onFolderClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }
            holder.bindView(getItem(position)!!)
        }
    }

    /** This method to update the layout */
    fun changeViewType() {
        viewType = if (viewType == ViewType.LINEAR) {
            ViewType.GRID
        } else {
            ViewType.LINEAR
        }
        notifyItemRangeChanged(0, itemCount)
    }
}
