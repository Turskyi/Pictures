package com.turskyi.gallery.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

class GalleryViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

//{
//    //TODO холдер є тільки для доступу до айтемів, в ньому не відбувається жодних оголошень, бо вони мінливі
//    // занесення даних повинне відбуватися в он бінд, коли холдер повертається на екран
//    // крім того об'єкти холдери можуть використовуватися повторно з іншими ресурсами. роблячи так ти блокуєш цю функцію
//    private val fileNameTV: TextView = itemView.findViewById(R.id.file_name)
//    private val previewIV: ImageView = itemView.findViewById(R.id.file_iv_preview)
//
//    /* I left this object to perform onLongClickListener otherwise it is not gonna work */
//    private val selectedImage: ImageView = itemView.findViewById(R.id.selected_image)
//
//    fun bindView(aFile: MyFile, aContext: Context) {
//        fileNameTV.text = aFile.name
//
//        itemView.setOnLongClickListener(imageLongClickListener)
//
//        if (aFile.extension in listOf("jpeg", "png", "jpg", "webp", "JPEG", "PNG", "JPG")) {
//            val aBitmap = BitmapFactory.decodeFile(aFile.path)
//            previewIV.scaleType = ImageView.ScaleType.CENTER_CROP
//
//            previewIV.setImageBitmap(aBitmap)
//
//            previewIV.setOnClickListener {
//                FileLiveSingleton.getInstance().setPath(aFile.path)
//            }
//
//            /** to use for opening the picture */
//            itemView.setOnClickListener {
//                //                    val anIntent = Intent(aContext, DetailActivity::class.java)
//                val anIntent = Intent(aContext, DetailFragment::class.java)
//                anIntent.putExtra("File", aFile.path)
//                aContext.startActivity(anIntent)
//            }
//        } else {
//
//            /** if image exists in folder */
//
////            aPicture.imageFile?.let {
////                //                        aPicture.imageFile?.let {aPicture ->
//////                            val path: String = it.imageFile!!.path
////                val path: String = aPicture.imageFile.path
////                val myBitmap = BitmapFactory.decodeFile(path)
////                previewIV.scaleType = ImageView.ScaleType.CENTER_CROP
////                previewIV.setImageBitmap(myBitmap)
////            }
//
//            itemView.setOnClickListener {
//                FileLiveSingleton.getInstance().setPath(aFile.path)
//            }
//        }
//    }
//
//    private var imageLongClickListener: View.OnLongClickListener = View.OnLongClickListener {
//
//        if (selectedImage.visibility == View.INVISIBLE) {
//            selectedImage.visibility = View.VISIBLE
//        } else {
//            selectedImage.visibility = View.INVISIBLE
//        }
//        true
//    }
//}