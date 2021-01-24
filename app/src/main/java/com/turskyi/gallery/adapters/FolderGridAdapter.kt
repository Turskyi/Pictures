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

class FolderGridAdapter(
    private val onFolderLongClickListener: OnFolderLongClickListener
) : PagedListAdapter<Folder, RecyclerView.ViewHolder>(FOLDERS_DIFF_CALLBACK) {

    private var viewType: ViewType = ViewType.GRID

    /**
     * @Description switch between layouts
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FolderGridViewHolder(parent)
    }

    /**
     * @Description making "check" sign visible and invisible onLongClick.
     * Makes the cover of a folder with a picture
     * */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FolderGridViewHolder) {

            holder.ivFolderPreview.setOnLongClickListener {
                if (holder.ivFolderSelected.visibility == View.INVISIBLE) {
                    holder.ivFolderSelected.visibility = View.VISIBLE
                    onFolderLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.ivFolderSelected.visibility = View.INVISIBLE
                    onFolderLongClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }

            holder.itemView.setOnLongClickListener {
                if (holder.ivFolderSelected.visibility == View.INVISIBLE) {
                    holder.ivFolderSelected.visibility = View.VISIBLE
                    onFolderLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.ivFolderSelected.visibility = View.INVISIBLE
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
