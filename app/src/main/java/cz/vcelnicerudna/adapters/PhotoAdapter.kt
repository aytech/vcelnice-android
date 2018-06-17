package cz.vcelnicerudna.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.vcelnicerudna.GlideApp
import cz.vcelnicerudna.PhotoGalleryActivity
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.models.Photo

class PhotoAdapter(var context: Context, private var dataSet: Array<Photo>) : RecyclerView.Adapter<PhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val imageView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.fragment_photo, parent, false) as View
        return PhotoViewHolder(imageView)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item: Photo = dataSet[position]
        if (item.thumb != "") {
            GlideApp
                    .with(context)
                    .load(APIConstants.VCELNICE_BASE_URL + item.thumb)
                    .placeholder(R.mipmap.ic_bee)
                    .fitCenter()
                    .into(holder.imageView)
            holder.imageView.setOnClickListener {
                val intent = Intent(context, PhotoGalleryActivity::class.java)
                intent.putParcelableArrayListExtra(StringConstants.PHOTOS_KEY, dataSet.toCollection(ArrayList()))
                intent.putExtra(StringConstants.PHOTO_POSITION, position)
//                intent.putExtra(StringConstants.PHOTO_KEY, item)
                context.startActivity(intent)
            }
        }
    }

    fun loadNewData(photo: Array<Photo>) {
        this.dataSet = photo
        notifyDataSetChanged()
    }
}