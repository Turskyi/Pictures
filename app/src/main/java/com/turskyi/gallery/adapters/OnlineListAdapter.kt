package com.turskyi.gallery.adapters

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.data.DiffUtilComparators.ONLINE_PICTURES_DIFF_CALLBACK
import com.turskyi.gallery.models.OnlinePictureRepo
import com.turskyi.gallery.interfaces.OnOnlinePictureLongClickListener
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewholders.OnlineGridViewHolder
import com.turskyi.gallery.viewholders.OnlineListViewHolder

class OnlineListAdapter constructor(
    private val onPictureLongClickListener: OnOnlinePictureLongClickListener
) : PagedListAdapter<OnlinePictureRepo, RecyclerView.ViewHolder>(ONLINE_PICTURES_DIFF_CALLBACK) {

    private var viewType: ViewType = ViewType.LINEAR

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (this.viewType == ViewType.GRID) {
            OnlineGridViewHolder(parent)
        } else {
            OnlineListViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OnlineGridViewHolder) {
            /** making "check sign visible and invisible onLongClick */
            holder.previewIV.setOnLongClickListener {
                if (holder.selectedImage.visibility == View.INVISIBLE) {
                    holder.selectedImage.visibility = View.VISIBLE
                    onPictureLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedImage.visibility = View.INVISIBLE
                    onPictureLongClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }
            holder.itemView.setOnLongClickListener {
                if (holder.selectedImage.visibility == View.INVISIBLE) {
                    holder.selectedImage.visibility = View.VISIBLE
                    onPictureLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedImage.visibility = View.INVISIBLE
                    onPictureLongClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }
            holder.bindView(getItem(position)!!)
        } else if (holder is OnlineListViewHolder) {
            /** making "check sign visible and invisible onLongClick */
            holder.previewIV.setOnLongClickListener {
                if (holder.selectedImage.visibility == View.INVISIBLE) {
                    holder.selectedImage.visibility = View.VISIBLE
                    onPictureLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedImage.visibility = View.INVISIBLE
                    onPictureLongClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }
            holder.itemView.setOnLongClickListener {
                if (holder.selectedImage.visibility == View.INVISIBLE) {
                    holder.selectedImage.visibility = View.VISIBLE
                    onPictureLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedImage.visibility = View.INVISIBLE
                    onPictureLongClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }
            holder.bindView(getItem(position)!!)
        }
    }

    fun changeViewType() {
        viewType = if (viewType == ViewType.LINEAR) ViewType.GRID
        else ViewType.LINEAR
        notifyItemRangeChanged(0, itemCount)
    }
}

