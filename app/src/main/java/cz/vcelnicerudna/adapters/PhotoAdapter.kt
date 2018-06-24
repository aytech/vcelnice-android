package cz.vcelnicerudna.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.vcelnicerudna.GlideApp
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.interfaces.PhotoItemClickListener
import cz.vcelnicerudna.models.Photo

class PhotoAdapter(var context: Context, private var dataSet: ArrayList<Photo>,
                   private var photoItemClickListener: PhotoItemClickListener) : RecyclerView.Adapter<PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val imageView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.fragment_photo, parent, false) as View
        return PhotoViewHolder(imageView)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo: Photo = dataSet[position]
        if (photo.thumb != "") {
            GlideApp
                    .with(context)
                    .load(APIConstants.VCELNICE_BASE_URL + photo.thumb)
                    .placeholder(R.mipmap.ic_bee)
                    .fitCenter()
                    .into(holder.imageView)
            holder.imageView.setOnClickListener {
                photoItemClickListener.onPhotoItemClickListener(holder.adapterPosition, photo, holder.imageView)
            }
        }
    }
}