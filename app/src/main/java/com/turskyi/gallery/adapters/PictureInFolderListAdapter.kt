package com.turskyi.gallery.adapters

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.interfaces.OnPictureClickListener
import com.turskyi.gallery.models.GalleryPicture
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewholders.PictureListViewHolder
import com.turskyi.gallery.viewholders.PictureStaggeredViewHolder

class PictureInFolderListAdapter(
    private val onPictureClickListener: OnPictureClickListener
) : PagedListAdapter<GalleryPicture, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<GalleryPicture> =
            object : DiffUtil.ItemCallback<GalleryPicture>() {
                override fun areItemsTheSame(
                    oldItem: GalleryPicture,
                    newItem: GalleryPicture
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: GalleryPicture,
                    newItem: GalleryPicture
                ): Boolean {
                    return oldItem.path == newItem.path
                }
            }
    }

    private var viewType: ViewType = ViewType.LINEAR

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
                    onPictureClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedImage.visibility = View.INVISIBLE
                    onPictureClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }

            holder.itemView.setOnLongClickListener {
                if (holder.selectedImage.visibility == View.INVISIBLE) {
                    holder.selectedImage.visibility = View.VISIBLE
                    onPictureClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedImage.visibility = View.INVISIBLE
                    onPictureClickListener.removeOnLongClick(
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
                    onPictureClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedImage.visibility = View.INVISIBLE
                    onPictureClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }

            holder.itemView.setOnLongClickListener {
                if (holder.selectedImage.visibility == View.INVISIBLE) {
                    holder.selectedImage.visibility = View.VISIBLE
                    onPictureClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.selectedImage.visibility = View.INVISIBLE
                    onPictureClickListener.removeOnLongClick(
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
        viewType = if (viewType == ViewType.LINEAR) ViewType.STAGGERED
        else ViewType.LINEAR
        notifyItemRangeChanged(0, itemCount)
    }
}