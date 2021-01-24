package com.turskyi.gallery.adapters

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.turskyi.gallery.data.DiffUtilComparators.ONLINE_PICTURES_DIFF_CALLBACK
import com.turskyi.gallery.interfaces.OnOnlinePictureLongClickListener
import com.turskyi.gallery.models.OnlinePictureRepo
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewholders.OnlineGridViewHolder

class OnlineGridAdapter constructor(
    private val onPictureLongClickListener: OnOnlinePictureLongClickListener
) : PagedListAdapter<OnlinePictureRepo, RecyclerView.ViewHolder>(ONLINE_PICTURES_DIFF_CALLBACK) {

    private var viewType: ViewType = ViewType.GRID

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OnlineGridViewHolder(parent)
    }

    /**
     * @Description making "check" sign visible and invisible onLongClick.
     * */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OnlineGridViewHolder) {

            holder.ivPicturePreview.setOnLongClickListener {
                if (holder.ivSelectedPicture.visibility == View.INVISIBLE) {
                    holder.ivSelectedPicture.visibility = View.VISIBLE
                    onPictureLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.ivSelectedPicture.visibility = View.INVISIBLE
                    onPictureLongClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }
            holder.itemView.setOnLongClickListener {
                if (holder.ivSelectedPicture.visibility == View.INVISIBLE) {
                    holder.ivSelectedPicture.visibility = View.VISIBLE
                    onPictureLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.ivSelectedPicture.visibility = View.INVISIBLE
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
