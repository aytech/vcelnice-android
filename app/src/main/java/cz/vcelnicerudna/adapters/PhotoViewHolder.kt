package cz.vcelnicerudna.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import cz.vcelnicerudna.R

class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageView: ImageView = itemView.findViewById(R.id.photo_item)
}