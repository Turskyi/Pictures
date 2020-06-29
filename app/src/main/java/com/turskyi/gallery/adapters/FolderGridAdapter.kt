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
import kotlinx.android.synthetic.main.folder_item.view.*

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

            holder.itemView.folderPreviewIV.setOnLongClickListener {
                if (holder.itemView.selectedFolder.visibility == View.INVISIBLE) {
                    holder.itemView.selectedFolder.visibility = View.VISIBLE
                    onFolderLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.itemView.selectedFolder.visibility = View.INVISIBLE
                    onFolderLongClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }

            holder.itemView.setOnLongClickListener {
                if (holder.itemView.selectedFolder.visibility == View.INVISIBLE) {
                    holder.itemView.selectedFolder.visibility = View.VISIBLE
                    onFolderLongClickListener.addOnLongClick(getItem(position)!!)
                } else {
                    holder.itemView.selectedFolder.visibility = View.INVISIBLE
                    onFolderLongClickListener.removeOnLongClick(
                        getItem(position)!!,
                        viewType
                    )
                }
                true
            }

            holder.bindView(getItem(position)!!)

//            val file = File(getItem(position)!!.firstPicturePath)
//            holder.itemView.folderName.text = getItem(position)!!.name
//            val uri: Uri = Uri.fromFile(file)
//            Glide.with(holder.itemView.context).load(uri).into(holder.itemView.folderPreviewIV)
//
//            holder.itemView.folderPreviewIV.setOnClickListener {
//                val fragmentManager: FragmentTransaction =
//                    (holder.itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
//                val picturesInFolderFragment =
//                    PicturesInFolderFragment(getItem(position)!!)
//                fragmentManager
//                    .replace(
//                        R.id.container,
//                        picturesInFolderFragment,
//                        GalleryConstants.TAG_PICS_IN_FOLDER
//                    )
//                    .addToBackStack(GalleryConstants.TAG_PICS_IN_FOLDER).commit()
//            }
//
//            holder.itemView.setOnClickListener {
//                val fragmentManager: FragmentTransaction =
//                    (holder.itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
//                val picturesInFolderFragment =
//                    PicturesInFolderFragment(getItem(position)!!)
//                fragmentManager
//                    .replace(
//                        R.id.container,
//                        picturesInFolderFragment,
//                        GalleryConstants.TAG_PICS_IN_FOLDER
//                    )
//                    .addToBackStack(GalleryConstants.TAG_PICS_IN_FOLDER).commit()
//            }
//
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
