package com.turskyi.gallery.adapters

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.data.DiffUtilComparators.FOLDERS_DIFF_CALLBACK
import com.turskyi.gallery.interfaces.OnFolderLongClickListener
import com.turskyi.gallery.models.Folder
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewholders.FolderGridViewHolder
import com.turskyi.gallery.viewholders.FolderListViewHolder

class FolderListAdapter(
    private val onFolderLongClickListener: OnFolderLongClickListener
) : PagedListAdapter<Folder, RecyclerView.ViewHolder>(FOLDERS_DIFF_CALLBACK) {

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
                    onFolderLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedFolder.visibility = View.INVISIBLE
                    onFolderLongClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }

            holder.itemView.setOnLongClickListener {
                if (holder.selectedFolder.visibility == View.INVISIBLE) {
                    holder.selectedFolder.visibility = View.VISIBLE
                    onFolderLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedFolder.visibility = View.INVISIBLE
                    onFolderLongClickListener.removeOnLongClick(
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
                    onFolderLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedFolder.visibility = View.INVISIBLE
                    onFolderLongClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }

            holder.itemView.setOnLongClickListener {
                if (holder.selectedFolder.visibility == View.INVISIBLE) {
                    holder.selectedFolder.visibility = View.VISIBLE
                    onFolderLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedFolder.visibility = View.INVISIBLE
                    onFolderLongClickListener.removeOnLongClick(
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
