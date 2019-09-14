package com.turskyi.gallery.adapters

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.data.DiffUtilComparators.PICTURES_DIFF_CALLBACK
import com.turskyi.gallery.interfaces.OnPictureLongClickListener
import com.turskyi.gallery.models.Picture
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewholders.PictureListViewHolder
import com.turskyi.gallery.viewholders.PictureStaggeredViewHolder

class PictureInFolderStaggeredAdapter(
    private val onPictureLongClickListener: OnPictureLongClickListener
) : PagedListAdapter<Picture, RecyclerView.ViewHolder>(PICTURES_DIFF_CALLBACK) {

    private var viewType: ViewType = ViewType.STAGGERED

    /** switch between layouts */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (this.viewType == ViewType.STAGGERED) {
            PictureStaggeredViewHolder(parent)
        } else {
            PictureListViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is PictureStaggeredViewHolder) {
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
        } else if (holder is PictureListViewHolder) {
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
        viewType = if (viewType.id == ViewType.LINEAR.id) ViewType.STAGGERED
        else ViewType.LINEAR
        notifyItemRangeChanged(0, itemCount)
    }
}