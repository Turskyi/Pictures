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
import com.turskyi.gallery.data.DiffUtilComparators.PICTURES_DIFF_CALLBACK
import com.turskyi.gallery.data.GalleryConstants
import com.turskyi.gallery.fragments.DetailedFragment
import com.turskyi.gallery.interfaces.OnPictureLongClickListener
import com.turskyi.gallery.models.PictureUri
import com.turskyi.gallery.models.ViewType
import com.turskyi.gallery.viewholders.PictureGridViewHolder

class PictureGridAdapter(
    private val onPictureLongClickListener: OnPictureLongClickListener
) : PagedListAdapter<PictureUri, RecyclerView.ViewHolder>(PICTURES_DIFF_CALLBACK) {

    private var viewType: ViewType = ViewType.GRID

    /**
     * @Description switch between layouts
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PictureGridViewHolder(parent)
    }

    /**
     * @Description making "check" sign visible and invisible onLongClick
     * */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PictureGridViewHolder) {

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

            val uri: Uri? = getItem(position)?.uri
            Glide.with(holder.itemView.context)
                .load(uri)
                .into(holder.ivPicturePreview)

            holder.ivPicturePreview.setOnClickListener {
                val fragmentManager: FragmentTransaction =
                    (holder.itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val detailedFragment = DetailedFragment(getItem(position)!!)
                fragmentManager
                    .replace(
                        R.id.container,
                        detailedFragment,
                        GalleryConstants.TAG_DETAILED_FRAGMENT
                    )
                    .addToBackStack(GalleryConstants.TAG_DETAILED_FRAGMENT).commit()
            }

            holder.itemView.setOnClickListener {
                val fragmentManager: FragmentTransaction =
                    (holder.itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val detailedFragment = DetailedFragment(getItem(position)!!)
                fragmentManager
                    .replace(
                        R.id.container,
                        detailedFragment,
                        GalleryConstants.TAG_DETAILED_FRAGMENT
                    )
                    .addToBackStack(GalleryConstants.TAG_DETAILED_FRAGMENT).commit()
            }

        }
    }

    fun changeViewType() {
        viewType = if (viewType == ViewType.LINEAR) ViewType.GRID
        else ViewType.LINEAR
        notifyItemRangeChanged(0, itemCount)
    }
}