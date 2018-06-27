package cz.vcelnicerudna.adapters

import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import cz.vcelnicerudna.configuration.APIConstants
import cz.vcelnicerudna.models.Photo
import cz.vcelnicerudna.views.AspectRatioImageView

class PhotoAdapter(
        private var dataSet: ArrayList<Photo>,
        private val onItemClickListener: OnItemClickListener?)
    : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position], onItemClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    fun loadData(photos: ArrayList<Photo>) {
        dataSet = photos
        notifyDataSetChanged()
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            AspectRatioImageView(parent.context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }) {
        fun bind(photo: Photo, onItemClickListener: OnItemClickListener?) {
            itemView.setOnClickListener { onItemClickListener?.onClick(photo, it) }
            itemView.tag = Photo.transitionName(photo.id)
            ViewCompat.setTransitionName(itemView, Photo.transitionName(photo.id))
            if (!photo.thumb.isEmpty()) {
                Picasso
                        .with(itemView.context)
                        .load(APIConstants.VCELNICE_BASE_URL + photo.thumb)
                        .into(itemView as ImageView)
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(item: Photo, view: View)
    }
}